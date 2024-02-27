package lucaswolschick.moa_scp_mh.parser;

import java.util.ArrayList;
import java.util.List;

public class Instancia {
    private final int nLinhas;
    private final int nColunas;
    private final double densidade;
    private final List<Coluna> dados;

    public Instancia(int n_linhas, int n_colunas, double densidade, List<Coluna> dados) {
        this.nLinhas = n_linhas;
        this.nColunas = n_colunas;
        this.densidade = densidade;
        this.dados = dados;
    }

    private List<List<Integer>> colunasCobrindoLinha;

    public List<List<Integer>> colunasCobrindoLinha() {
        if (colunasCobrindoLinha == null) {
            var res = new ArrayList<List<Integer>>();
            for (int linha = 1; linha <= nLinhas; linha++) {
                var colunas = new ArrayList<Integer>();
                res.add(colunas);
            }
            for (var coluna : dados) {
                for (var linha : coluna.elem()) {
                    res.get(linha - 1).add(coluna.id());
                }
            }
            colunasCobrindoLinha = res;
        }
        return colunasCobrindoLinha;
    }

    public int nLinhas() {
        return nLinhas;
    }

    public int nColunas() {
        return nColunas;
    }

    public double densidade() {
        return densidade;
    }

    public List<Coluna> dados() {
        return dados;
    }

    @Override
    public String toString() {
        return "Instancia [n_linhas=" + nLinhas + ", n_colunas=" + nColunas + ", densidade=" + densidade + ", dados="
                + dados + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + nLinhas;
        result = prime * result + nColunas;
        long temp;
        temp = Double.doubleToLongBits(densidade);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((dados == null) ? 0 : dados.hashCode());
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
        Instancia other = (Instancia) obj;
        if (nLinhas != other.nLinhas)
            return false;
        if (nColunas != other.nColunas)
            return false;
        if (Double.doubleToLongBits(densidade) != Double.doubleToLongBits(other.densidade))
            return false;
        if (dados == null) {
            if (other.dados != null)
                return false;
        } else if (!dados.equals(other.dados))
            return false;
        return true;
    }
}