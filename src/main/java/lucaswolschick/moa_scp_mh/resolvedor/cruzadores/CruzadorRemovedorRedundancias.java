package lucaswolschick.moa_scp_mh.resolvedor.cruzadores;

import java.util.HashSet;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores;
import lucaswolschick.moa_scp_mh.resolvedor.Solucao;

public class CruzadorRemovedorRedundancias implements Operadores.Cruzamento {
    @Override
    public Solucao cruza(Solucao gerador0, Solucao gerador1, Instancia instancia) {
        var colunas = new HashSet<>(gerador0.getColunas());
        colunas.addAll(gerador1.getColunas());
        var solucao = new Solucao(colunas, instancia);
        // remove redundâncias
        return solucao.removeRedundantes();
    }
}
