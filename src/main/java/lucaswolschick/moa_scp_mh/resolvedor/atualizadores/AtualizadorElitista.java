package lucaswolschick.moa_scp_mh.resolvedor.atualizadores;

import java.util.ArrayList;
import java.util.List;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores;
import lucaswolschick.moa_scp_mh.resolvedor.Solucao;

public class AtualizadorElitista implements Operadores.Atualizacao {
    private double fracElitista;
    private int tamanhoPopulacao;

    public AtualizadorElitista(double fracElitista, int tamanhoPopulacao) {
        this.fracElitista = fracElitista;
        this.tamanhoPopulacao = tamanhoPopulacao;
    }

    @Override
    public List<Solucao> atualiza(List<Solucao> pop, List<Solucao> descendentes, Instancia instancia) {
        var novaPop = new ArrayList<Solucao>(pop);

        pop = new ArrayList<>(pop);
        pop.sort((var a, var b) -> Double.compare(a.getCusto(), b.getCusto()));
        int nElite = (int) (fracElitista * tamanhoPopulacao);
        int nFilhos = tamanhoPopulacao - nElite;

        var elite = pop.subList(0, nElite);

        descendentes = new ArrayList<>(descendentes);
        descendentes.sort((var a, var b) -> Double.compare(a.getCusto(), b.getCusto()));

        var descs = descendentes.subList(0, nFilhos);
        novaPop.addAll(elite);
        novaPop.addAll(descs);
        return novaPop;
    }

    @Override
    public String toString() {
        return "AtualizadorElitista [fracElitista=" + fracElitista + ", tamanhoPopulacao=" + tamanhoPopulacao + "]";
    }

}
