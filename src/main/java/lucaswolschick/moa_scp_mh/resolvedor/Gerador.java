package lucaswolschick.moa_scp_mh.resolvedor;

import java.util.List;

import lucaswolschick.moa_scp_mh.parser.Instancia;

public interface Gerador {
    public List<Solucao> geraPopulacaoInicial(Instancia instancia, int tamanhoPopulacao, long seed);
}
