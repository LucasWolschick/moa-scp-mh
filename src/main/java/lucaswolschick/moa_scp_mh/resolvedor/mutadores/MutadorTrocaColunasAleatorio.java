package lucaswolschick.moa_scp_mh.resolvedor.mutadores;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores;
import lucaswolschick.moa_scp_mh.resolvedor.Solucao;

public class MutadorTrocaColunasAleatorio implements Operadores.Mutacao {

    private double probMutacaoColuna;

    public MutadorTrocaColunasAleatorio(double probMutacaoColuna) {
        this.probMutacaoColuna = probMutacaoColuna;
    }

    @Override
    public List<Solucao> muta(List<Solucao> cruzamentos, Instancia instancia) {
        var random = ThreadLocalRandom.current();
        var solucoes = new ArrayList<>(cruzamentos);
        var novasSolucoes = new ArrayList<Solucao>();
        for (var solucao : solucoes) {
            var colunas = new ArrayList<>(solucao.getColunas());
            var colunasNovaSolucao = new HashSet<Integer>();
            for (int i = 0; i < colunas.size(); i++) {
                if (random.nextDouble() < probMutacaoColuna) {
                    var novasColunas = mutaColuna(colunas, i, instancia);
                    colunasNovaSolucao.addAll(novasColunas);
                } else {
                    colunasNovaSolucao.add(colunas.get(i));
                }
            }
            novasSolucoes.add(new Solucao(colunasNovaSolucao, instancia).removeRedundantes());
        }
        return novasSolucoes;
    }

    private HashSet<Integer> mutaColuna(List<Integer> colunas, int iColuna, Instancia instancia) {
        var random = ThreadLocalRandom.current();

        // escolhe uma coluna aleatoriamente
        var colRemovida = colunas.remove(iColuna);

        var linhasDescobertas = IntStream.rangeClosed(1, instancia.nLinhas()).boxed().collect(Collectors.toSet());
        for (var col : colunas) {
            linhasDescobertas.removeAll(instancia.dados().get(col - 1).elem());
        }
        colunas.add(colRemovida);

        // invariante: colunasComLinhasDescobertas contém todas as colunas que possuem
        // ao menos alguma linha em linhasDescobertas.
        var colunasComLinhasDescobertas = new ArrayList<Integer>();
        for (var col : colunas) {
            for (var linha : linhasDescobertas) {
                if (instancia.dados().get(col - 1).elem().contains(linha)) {
                    colunasComLinhasDescobertas.add(col);
                    break;
                }
            }
        }

        while (!linhasDescobertas.isEmpty()) {
            // escolhe uma coluna das candidatas
            int colunaEscolhida = colunasComLinhasDescobertas
                    .remove(random.nextInt(colunasComLinhasDescobertas.size()));
            // adiciona a coluna
            colunas.add(colunaEscolhida);

            // atualiza linhasDescobertas
            linhasDescobertas.removeAll(instancia.dados().get(colunaEscolhida - 1).elem());
            // atualiza colunasComLinhasDescobertas
            colunasComLinhasDescobertas.clear();
            for (var col : colunas) {
                for (var linha : linhasDescobertas) {
                    if (instancia.dados().get(col - 1).elem().contains(linha)) {
                        colunasComLinhasDescobertas.add(col);
                        break;
                    }
                }
            }
        }

        return new HashSet<>(colunas);
    }
}
