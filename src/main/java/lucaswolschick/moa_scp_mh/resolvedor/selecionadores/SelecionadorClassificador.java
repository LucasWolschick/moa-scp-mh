package lucaswolschick.moa_scp_mh.resolvedor.selecionadores;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores;
import lucaswolschick.moa_scp_mh.resolvedor.Solucao;

public class SelecionadorClassificador implements Operadores.Selecao {
    @Override
    public Solucao[] seleciona(Map<Solucao, Double> avaliacao, Instancia instancia) {
        var sortidos = avaliacao.entrySet().parallelStream()
                .sorted((a, b) -> a.getValue().compareTo(b.getValue()))
                .map(e -> e.getKey())
                .collect(Collectors.toList());

        int numSortidos = sortidos.size();
        int max = (numSortidos * (numSortidos + 1)) / 2;

        var sol1 = getI(sortidos, numSortidos, max);
        var sol2 = getI(sortidos, numSortidos, max);
        while (sol1 == sol2) {
            sol2 = getI(sortidos, numSortidos, max);
        }

        return new Solucao[] { sol1, sol2 };
    }

    private Solucao getI(List<Solucao> sortidos, int numSortidos, int max) {
        var random = ThreadLocalRandom.current();

        int sorteio = random.nextInt(max);
        int i = 0;
        for (i = 0; i < numSortidos && sorteio >= 0; ++i, sorteio -= (numSortidos - i)) {
        }
        if (i == numSortidos)
            i -= 1;
        return sortidos.get(i);
    }
}
