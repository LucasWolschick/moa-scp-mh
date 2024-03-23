package lucaswolschick.moa_scp_mh.resolvedor.selecionadores;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores;
import lucaswolschick.moa_scp_mh.resolvedor.Solucao;
import lucaswolschick.moa_scp_mh.resolvedor.ThreadRandomFactory;

public class SelecionadorClassificador implements Operadores.Selecao {
    @Override
    public Solucao[] seleciona(Map<Solucao, Double> avaliacao, Instancia instancia, long semente) {
        var sortidos = avaliacao.entrySet().parallelStream()
                .sorted((a, b) -> a.getValue().compareTo(b.getValue()))
                .map(e -> e.getKey())
                .collect(Collectors.toList());

        int numSortidos = sortidos.size();
        int max = (numSortidos * (numSortidos + 1)) / 2;

        var r = ThreadRandomFactory.getRandom(semente);
        var sol1 = getI(sortidos, numSortidos, max, r);
        var sol2 = getI(sortidos, numSortidos, max, r);
        while (sol1 == sol2) {
            sol2 = getI(sortidos, numSortidos, max, r);
        }

        return new Solucao[] { sol1, sol2 };
    }

    private Solucao getI(List<Solucao> sortidos, int numSortidos, int max, Random random) {
        int sorteio = random.nextInt(max);
        int i = 0;
        for (i = 0; i < numSortidos && sorteio >= 0; ++i, sorteio -= (numSortidos - i)) {
        }
        if (i == numSortidos)
            i -= 1;
        return sortidos.get(i);
    }

    @Override
    public String toString() {
        return "SelecionadorClassificador []";
    }

}
