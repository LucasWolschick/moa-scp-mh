package lucaswolschick.moa_scp_mh.resolvedor.buscaLocal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores;
import lucaswolschick.moa_scp_mh.resolvedor.Solucao;

public class BuscaLocal implements Operadores.BuscaLocal {
    private final Estrategia estrategia;
    private final int colunasTrocadas;

    public static enum Estrategia {
        FIRST_IMPROVEMENT, BEST_IMPROVEMENT
    }

    public BuscaLocal(Estrategia estrategia, int colunasTrocadas) {
        this.estrategia = estrategia;
        this.colunasTrocadas = colunasTrocadas;
    }

    @Override
    public List<Solucao> buscaLocal(List<Solucao> pop, Instancia instancia, long _seed) {
        var allColunas = IntStream.rangeClosed(1, instancia.nColunas()).boxed().collect(Collectors.toList());
        return IntStream.range(0, pop.size()).parallel()
                .mapToObj((var i) -> melhoraSolucao(allColunas, pop.get(i), instancia))
                .collect(Collectors.toList());
    }

    private Solucao melhoraSolucao(List<Integer> allColunas, Solucao solucao, Instancia instancia) {
        Solucao s = solucao;

        for (;;) {
            var novaSolucao = melhoraSolucaoPasso(allColunas, s, instancia);
            if (novaSolucao.getCusto() < s.getCusto()) {
                s = novaSolucao;
            } else {
                break;
            }
        }

        return s;
    }

    private Solucao melhoraSolucaoPasso(List<Integer> allColunas, Solucao solucao, Instancia instancia) {
        var vizinhos = new ArrayList<Solucao>();

        var solucaoColunas = solucao.getColunas();
        var colunas = new ArrayList<Integer>(solucaoColunas.size());
        for (var col : solucaoColunas) {
            colunas.add(col);
        }

        var colunasCobrindoLinha = new int[instancia.nLinhas()];
        var linhasDescobertas = instancia.nLinhas();
        for (var col : colunas) {
            for (var elem : instancia.dados().get(col - 1).elem()) {
                if (colunasCobrindoLinha[elem - 1] == 0) {
                    linhasDescobertas -= 1;
                }
                colunasCobrindoLinha[elem - 1] += 1;
            }
        }

        for (int numNovasColunas = 0; numNovasColunas <= colunasTrocadas; numNovasColunas++) {
            for (var coluna : colunas) {
                var novasColunas = new HashSet<Integer>(colunas);
                var novasColunasCobrindoLinhas = colunasCobrindoLinha.clone();
                var novasLinhasDescobertas = linhasDescobertas;

                novasColunas.remove(coluna);
                for (var elem : instancia.dados().get(coluna - 1).elem()) {
                    novasColunasCobrindoLinhas[elem - 1] -= 1;
                    if (novasColunasCobrindoLinhas[elem - 1] == 0) {
                        novasLinhasDescobertas += 1;
                    }
                }

                var colunasNaoNaSolucao = new ArrayList<Integer>(allColunas.size());
                for (var c : allColunas) {
                    if (!novasColunas.contains(c)) {
                        colunasNaoNaSolucao.add(c);
                    }
                }

                // adiciona novas colunas
                for (var comb : new CombinationsIterator<>(colunasNaoNaSolucao, numNovasColunas)) {
                    var novaSolucao = new HashSet<>(novasColunas);
                    for (var c : comb) {
                        novaSolucao.add(c);
                        for (var elem : instancia.dados().get(c - 1).elem()) {
                            if (novasColunasCobrindoLinhas[elem - 1] == 0) {
                                novasLinhasDescobertas -= 1;
                            }
                            novasColunasCobrindoLinhas[elem - 1] += 1;
                        }
                    }
                    if (novasLinhasDescobertas == 0) {
                        var sol = Solucao.removeRedundantes(novaSolucao, instancia);
                        if (estrategia == Estrategia.FIRST_IMPROVEMENT) {
                            if (sol.getCusto() < solucao.getCusto()) {
                                return sol;
                            }
                        } else {
                            vizinhos.add(sol);
                        }
                    }
                    for (var c : comb) {
                        for (var elem : instancia.dados().get(c - 1).elem()) {
                            novasColunasCobrindoLinhas[elem - 1] -= 1;
                            if (novasColunasCobrindoLinhas[elem - 1] == 0) {
                                novasLinhasDescobertas += 1;
                            }
                        }
                    }
                }
            }
        }

        if (estrategia == Estrategia.FIRST_IMPROVEMENT) {
            return solucao;
        } else {
            if (vizinhos.isEmpty()) {
                return solucao;
            }

            var min = vizinhos.get(0);
            for (var v : vizinhos) {
                if (v.getCusto() < min.getCusto()) {
                    min = v;
                }
            }
            return min;
        }
    }

    @Override
    public String toString() {
        return "BuscaLocal [estrategia=" + estrategia + ", colunasTrocadas=" + colunasTrocadas + "]";
    }

}

class CombinationsIterator<T> implements Iterable<List<T>>, Iterator<List<T>> {
    private final List<T> elementos;
    private final int k;

    private int[] indices;
    private boolean hasNext = true;

    // evitar alocação de memória
    private ArrayList<T> result;

    public CombinationsIterator(List<T> elementos, int k) {
        this.elementos = elementos;
        this.k = k;
        this.indices = IntStream.range(0, k).toArray();
        this.result = new ArrayList<>(this.elementos.size());
    }

    @Override
    public Iterator<List<T>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public List<T> next() {
        result.clear();

        for (var i : indices) {
            result.add(elementos.get(i));
        }

        int i = k - 1;
        while (i >= 0 && indices[i] == elementos.size() - k + i) {
            i--;
        }

        if (i < 0) {
            hasNext = false;
        } else {
            indices[i]++;
            for (int j = i + 1; j < k; j++) {
                indices[j] = indices[j - 1] + 1;
            }
        }

        return result;
    }
}