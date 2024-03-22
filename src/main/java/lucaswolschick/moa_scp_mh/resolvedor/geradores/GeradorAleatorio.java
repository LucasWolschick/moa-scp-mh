package lucaswolschick.moa_scp_mh.resolvedor.geradores;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.resolvedor.Gerador;
import lucaswolschick.moa_scp_mh.resolvedor.Solucao;

public class GeradorAleatorio implements Gerador {
    private int tamanhoPopulacao;

    public GeradorAleatorio(int tamanhoPopulacao) {
        this.tamanhoPopulacao = tamanhoPopulacao;
    }

    @Override
    public List<Solucao> geraPopulacaoInicial(Instancia instancia) {
        return IntStream.range(0, tamanhoPopulacao).parallel()
                .mapToObj((var _i) -> geraSolucaoAleatoria(instancia))
                .collect(Collectors.toList());
    }

    private Solucao geraSolucaoAleatoria(Instancia instancia) {
        var random = ThreadLocalRandom.current();

        var colunasCobrindoLinha = new int[instancia.nLinhas()];
        var colunasSolucao = new HashSet<Integer>();
        var linhasNaoCobertas = instancia.nLinhas();
        var linhasDescobertasCobertasPorColuna = new int[instancia.nColunas()];
        for (var coluna : instancia.dados()) {
            linhasDescobertasCobertasPorColuna[coluna.id() - 1] = coluna.elem().size();
        }

        while (linhasNaoCobertas > 0) {
            var candidatos = instancia.dados().stream()
                    .filter((var c) -> linhasDescobertasCobertasPorColuna[c.id() - 1] > 0).collect(Collectors.toList());
            var colunaSortida = candidatos.get(random.nextInt(candidatos.size()));

            for (var linha : colunaSortida.elem()) {
                // atualiza linhasNaoCobertas
                if (colunasCobrindoLinha[linha - 1] == 0) {
                    linhasNaoCobertas -= 1;

                    // atualiza linhasDescobertasCobertasPorColuna
                    // modifica todas as colunas que tem essa linha (subtraindo 1 delas)
                    for (var coluna : instancia.colunasCobrindoLinha().get(linha - 1)) {
                        linhasDescobertasCobertasPorColuna[coluna - 1] -= 1;
                    }
                }
                // atualiza colunasCobrindoLinha
                colunasCobrindoLinha[linha - 1] += 1;
            }

            // atualiza colunasSolucao
            colunasSolucao.add(colunaSortida.id());
        }

        return new Solucao(colunasSolucao, instancia).removeRedundantes();
    }

    @Override
    public String toString() {
        return "GeradorAleatorio [tamanhoPopulacao=" + tamanhoPopulacao + "]";
    }

}
