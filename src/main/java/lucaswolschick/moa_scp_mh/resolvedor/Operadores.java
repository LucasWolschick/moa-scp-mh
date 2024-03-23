package lucaswolschick.moa_scp_mh.resolvedor;

import java.util.List;
import java.util.Map;

import lucaswolschick.moa_scp_mh.parser.Instancia;

public interface Operadores {
    public interface Avaliacao {
        Map<Solucao, Double> avalia(List<Solucao> pop, Instancia instancia, long semente);
    }

    public interface Selecao {
        Solucao[] seleciona(Map<Solucao, Double> avaliacao, Instancia instancia, long semente);
    }

    public interface Cruzamento {
        Solucao cruza(Solucao gerador0, Solucao gerador1, Instancia instancia, long semente);
    }

    public interface Mutacao {
        List<Solucao> muta(List<Solucao> cruzamentos, Instancia instancia, long semente);
    }

    public interface Atualizacao {
        List<Solucao> atualiza(List<Solucao> pop, List<Solucao> descendentes, int tamanhoPopulacao, Instancia instancia,
                long semente);
    }

    public interface BuscaLocal {
        List<Solucao> buscaLocal(List<Solucao> pop, Instancia instancia, long semente);
    }
}
