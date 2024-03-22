package lucaswolschick.moa_scp_mh.resolvedor.avaliadores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores;
import lucaswolschick.moa_scp_mh.resolvedor.Solucao;

public class AvaliadorCusto implements Operadores.Avaliacao {
    @Override
    public Map<Solucao, Double> avalia(List<Solucao> pop, Instancia instancia) {
        return pop.parallelStream().collect(
                HashMap::new,
                (map, s) -> map.put(s, s.getCusto()),
                HashMap::putAll);
    }

    @Override
    public String toString() {
        return "AvaliadorCusto []";
    }
}
