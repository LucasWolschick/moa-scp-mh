package lucaswolschick.moa_scp_mh.resolvedor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lucaswolschick.moa_scp_mh.parser.Instancia;

public class Solucao {
    private final Set<Integer> colunas;
    private final double custo;
    private final Instancia instancia;

    public Solucao(Set<Integer> colunas, Instancia instancia) {
        this.colunas = colunas;
        double custo = 0.0f;

        for (var col : colunas) {
            custo += instancia.dados().get(col - 1).custo();
        }

        this.custo = custo;
        this.instancia = instancia;
    }

    public Set<Integer> getColunas() {
        return colunas;
    }

    public double getCusto() {
        return custo;
    }

    @Override
    public String toString() {
        return "Solucao [colunas=" + colunas + ", custo=" + custo + "]";
    }

    public Solucao removeRedundantes() {
        var colunasOrdenadas = new ArrayList<>(colunas);
        colunasOrdenadas.sort((var l, var r) -> Double.compare(
                instancia.dados().get(l - 1).custo(),
                instancia.dados().get(r - 1).custo()));
        for (int i = colunasOrdenadas.size() - 1; i >= 0; i--) {
            int coluna = colunasOrdenadas.remove(i);
            if (!solucaoValida(colunasOrdenadas, instancia)) {
                colunasOrdenadas.add(coluna);
            }
        }
        return new Solucao(new HashSet<>(colunasOrdenadas), instancia);
    }

    public static boolean solucaoValida(List<Integer> colunas, Instancia instancia) {
        boolean[] linhas = new boolean[instancia.nLinhas()];
        int linhasFaltando = instancia.nLinhas();

        for (var col : colunas) {
            for (var elem : instancia.dados().get(col - 1).elem()) {
                if (!linhas[elem - 1]) {
                    linhasFaltando -= 1;
                    if (linhasFaltando == 0) {
                        return true;
                    }
                }
                linhas[elem - 1] = true;
            }
        }

        return false;
    }
}
