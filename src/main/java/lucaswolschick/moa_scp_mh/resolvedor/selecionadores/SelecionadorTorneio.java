package lucaswolschick.moa_scp_mh.resolvedor.selecionadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores;
import lucaswolschick.moa_scp_mh.resolvedor.Solucao;

public class SelecionadorTorneio implements Operadores.Selecao {
    private int k;

    public SelecionadorTorneio(int k) {
        this.k = k;
    }

    @Override
    public Solucao[] seleciona(Map<Solucao, Double> avaliacao, Instancia instancia) {
        var solucoes = new ArrayList<Solucao>(avaliacao.keySet());

        var sol1 = selecionaTorneio(solucoes, avaliacao);
        var sol2 = selecionaTorneio(solucoes, avaliacao);
        while (sol1 == sol2) {
            sol2 = selecionaTorneio(solucoes, avaliacao);
        }

        return new Solucao[] { sol1, sol2 };
    }

    private Solucao selecionaTorneio(List<Solucao> solucoes, Map<Solucao, Double> avaliacao) {
        var random = ThreadLocalRandom.current();

        var melhorSolucao = solucoes.get(0);
        for (int i = 0; i < k; i++) {
            var concorrente = solucoes.get(random.nextInt(solucoes.size()));
            if (avaliacao.get(concorrente) < avaliacao.get(melhorSolucao)) {
                melhorSolucao = concorrente;
            }
        }
        return melhorSolucao;
    }
}
