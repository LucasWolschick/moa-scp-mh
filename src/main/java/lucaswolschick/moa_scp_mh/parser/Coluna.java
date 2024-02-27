package lucaswolschick.moa_scp_mh.parser;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Coluna {
    private Set<Integer> elem;
    private double custo;
    private int id;

    public Coluna(int id, double custo, Set<Integer> elem) {
        this.id = id;
        this.custo = custo;
        this.elem = elem;
    }

    public static Coluna parseColuna(String s) {
        var lst = s.split("\\s+");
        var id = Integer.parseInt(lst[0]);
        var custo = Double.parseDouble(lst[1]);
        var colunas = Arrays.stream(lst)
                .skip(2)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
        return new Coluna(id, custo, colunas);
    }

    public Set<Integer> elem() {
        return elem;
    }

    public double custo() {
        return custo;
    }

    public int id() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((elem == null) ? 0 : elem.hashCode());
        long temp;
        temp = Double.doubleToLongBits(custo);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + id;
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
        Coluna other = (Coluna) obj;
        if (elem == null) {
            if (other.elem != null)
                return false;
        } else if (!elem.equals(other.elem))
            return false;
        if (Double.doubleToLongBits(custo) != Double.doubleToLongBits(other.custo))
            return false;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Coluna [elem=" + elem + ", custo=" + custo + ", id=" + id + "]";
    }
}