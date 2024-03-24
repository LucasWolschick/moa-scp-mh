package lucaswolschick.moa_scp_mh.resolvedor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

    public static Solucao removeRedundantes(Collection<Integer> colunas, Instancia instancia) {
        var colunasOrdenadas = new ArrayList<>(colunas);
        colunasOrdenadas.sort((var l, var r) -> Double.compare(
                instancia.dados().get(l - 1).custo(),
                instancia.dados().get(r - 1).custo()));

        int[] colunasCobrindoLinha = new int[instancia.nLinhas()];
        for (var col : colunasOrdenadas) {
            for (var elem : instancia.dados().get(col - 1).elem()) {
                colunasCobrindoLinha[elem - 1] += 1;
            }
        }

        for (int i = colunasOrdenadas.size() - 1; i >= 0; i--) {
            // swap remove
            // first swap
            int coluna = colunasOrdenadas.set(i, colunasOrdenadas.get(colunasOrdenadas.size() - 1));
            // then remove
            colunasOrdenadas.remove(colunasOrdenadas.size() - 1);

            var canRemove = true;
            var linhas = instancia.dados().get(coluna - 1).elem();
            for (var elem : linhas) {
                if (colunasCobrindoLinha[elem - 1] == 1) {
                    canRemove = false;
                    break;
                }
            }

            if (!canRemove) {
                colunasOrdenadas.add(coluna);
                continue;
            }

            for (var elem : linhas) {
                colunasCobrindoLinha[elem - 1] -= 1;
            }
        }
        return new Solucao(new HashSet<>(colunasOrdenadas), instancia);
    }

    public Solucao removeRedundantes() {
        return Solucao.removeRedundantes(colunas, instancia);
    }

    public static boolean solucaoValida(Collection<Integer> colunas, Instancia instancia) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((colunas == null) ? 0 : colunas.hashCode());
        long temp;
        temp = Double.doubleToLongBits(custo);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((instancia == null) ? 0 : instancia.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Solucao other = (Solucao) obj;
        if (colunas == null) {
            if (other.colunas != null)
                return false;
        } else if (!colunas.equals(other.colunas))
            return false;
        if (Double.doubleToLongBits(custo) != Double.doubleToLongBits(other.custo))
            return false;
        if (instancia == null) {
            if (other.instancia != null)
                return false;
        } else if (!instancia.equals(other.instancia))
            return false;
        return true;
    }

}
