package lucaswolschick.moa_scp_mh.resolvedor.mutadores;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores;
import lucaswolschick.moa_scp_mh.resolvedor.Solucao;
import lucaswolschick.moa_scp_mh.resolvedor.ThreadRandomFactory;

public class MutadorTrocaUmaColunaAleatorio implements Operadores.Mutacao {

    private double probMutacaoIndividuo;

    public MutadorTrocaUmaColunaAleatorio(double probMutacaoIndividuo) {
        this.probMutacaoIndividuo = probMutacaoIndividuo;
    }

    @Override
    public List<Solucao> muta(List<Solucao> cruzamentos, Instancia instancia, long semente) {
        var solucoes = new ArrayList<>(cruzamentos);
        var random = ThreadRandomFactory.getRandom(semente);
        return IntStream.range(0, solucoes.size()).parallel()
                .mapToObj((var i) -> random.nextDouble() < probMutacaoIndividuo
                        ? mutaSolucao(solucoes.get(i), instancia, ThreadRandomFactory.getRandom(semente, (long) i))
                        : solucoes.get(i))
                .collect(Collectors.toList());
    }

    private static Solucao mutaSolucao(Solucao solucao, Instancia instancia, Random random) {
        // escolhe uma coluna aleatoriamente
        var colunas = new ArrayList<>(solucao.getColunas());
        var colRemovida = colunas.remove(random.nextInt(colunas.size()));

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

        return new Solucao(new HashSet<>(colunas), instancia);
    }

    @Override
    public String toString() {
        return "MutadorTrocaUmaColunaAleatorio [probMutacaoIndividuo=" + probMutacaoIndividuo + "]";
    }

}
