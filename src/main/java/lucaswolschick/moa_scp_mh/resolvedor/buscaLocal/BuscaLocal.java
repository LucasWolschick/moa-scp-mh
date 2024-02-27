package lucaswolschick.moa_scp_mh.resolvedor.buscaLocal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
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
    public List<Solucao> buscaLocal(List<Solucao> pop, Instancia instancia) {
        return pop.parallelStream().map(s -> melhoraSolucao(s, instancia)).collect(Collectors.toList());
    }

    private Solucao melhoraSolucao(Solucao solucao, Instancia instancia) {
        Solucao s = solucao;

        for (;;) {
            var novaSolucao = melhoraSolucaoPasso(s, instancia);
            if (novaSolucao.getCusto() < s.getCusto()) {
                s = novaSolucao;
            } else {
                break;
            }
        }

        return s;
    }

    private Solucao melhoraSolucaoPasso(Solucao solucao, Instancia instancia) {
        var allColunas = IntStream.rangeClosed(1, instancia.nColunas()).boxed().collect(Collectors.toList());
        var vizinhos = new ArrayList<Solucao>();
        var colunas = new ArrayList<Integer>(solucao.getColunas());
        for (int numNovasColunas = 0; numNovasColunas <= colunasTrocadas; numNovasColunas++) {
            for (var coluna : colunas) {
                var novasColunas = new ArrayList<Integer>();

                // remove a coluna sorteada
                for (var col : colunas) {
                    if (col != coluna) {
                        novasColunas.add(col);
                    }
                }

                var colunasNaoNaSolucao = allColunas.stream().filter(c -> !novasColunas.contains(c))
                        .collect(Collectors.toList());

                // adiciona novas colunas
                for (var comb : combinations(colunasNaoNaSolucao, numNovasColunas)) {
                    var novaSolucao = new ArrayList<Integer>(novasColunas);
                    novaSolucao.addAll(comb);
                    if (Solucao.solucaoValida(novaSolucao, instancia)) {
                        var sol = new Solucao(new HashSet<>(novaSolucao), instancia).removeRedundantes();
                        if (estrategia == Estrategia.FIRST_IMPROVEMENT) {
                            if (sol.getCusto() < solucao.getCusto()) {
                                return sol;
                            }
                        } else {
                            vizinhos.add(sol);
                        }
                    }
                }
            }
        }

        if (estrategia == Estrategia.FIRST_IMPROVEMENT) {
            return solucao;
        } else {
            return vizinhos.stream().min(Comparator.comparing(Solucao::getCusto)).orElse(solucao);
        }
    }

    public static <T> List<List<T>> combinations(List<T> lst, int k) {
        if (k == 0) {
            List<List<T>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        } else {
            List<List<T>> result = new ArrayList<>();
            for (int i = 0; i < lst.size(); i++) {
                T val = lst.get(i);
                List<T> subList = lst.subList(i + 1, lst.size());
                List<List<T>> subResult = combinations(subList, k - 1);
                for (var sub : subResult) {
                    List<T> temp = new ArrayList<>();
                    temp.add(val);
                    temp.addAll(sub);
                    result.add(temp);
                }
            }
            return result;
        }
    }
}
