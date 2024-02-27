package lucaswolschick.moa_scp_mh.resolvedor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.resolvedor.atualizadores.AtualizadorElitista;
import lucaswolschick.moa_scp_mh.resolvedor.avaliadores.AvaliadorCusto;
import lucaswolschick.moa_scp_mh.resolvedor.buscaLocal.BuscaLocal;
import lucaswolschick.moa_scp_mh.resolvedor.cruzadores.CruzadorRemovedorRedundancias;
import lucaswolschick.moa_scp_mh.resolvedor.geradores.GeradorAleatorio;
import lucaswolschick.moa_scp_mh.resolvedor.mutadores.MutadorTrocaColunasAleatorio;
import lucaswolschick.moa_scp_mh.resolvedor.selecionadores.SelecionadorTorneio;

public class Resolvedor {
    private ResolvedorConfiguracao cfg;
    private Instancia instancia;
    private List<Solucao> pop;
    private int geracao;

    public Resolvedor(Instancia instancia, ResolvedorConfiguracao cfg) {
        this.cfg = cfg;
        this.instancia = instancia;
        this.pop = new ArrayList<>();
        this.geracao = 0;
    }

    public Resolvedor(Instancia instancia, int tamanhoPopulacao, int maxGeracoes) {
        this(instancia, new ResolvedorConfiguracao(
                tamanhoPopulacao,
                maxGeracoes,
                new GeradorAleatorio(tamanhoPopulacao),
                new AvaliadorCusto(),
                new SelecionadorTorneio(4),
                new CruzadorRemovedorRedundancias(),
                new MutadorTrocaColunasAleatorio(0.2f),
                new BuscaLocal(BuscaLocal.Estrategia.FIRST_IMPROVEMENT, 2),
                new AtualizadorElitista(0.1f, tamanhoPopulacao)));
    }

    public void resolve() {
        System.out.println("Gerando população inicial (geração 1)...");
        pop = geraPopulacaoInicial();
        System.out.println("(Custo = " + melhorSolucao().getCusto() + "; Custo médio = " + custoMedio() + ")");
        geracao += 1;

        while (!criterioDeParada()) {
            System.err.println("Geração " + (geracao + 1) + "...");
            pop = criaGeracao();
            System.out.println("(Custo = " + melhorSolucao().getCusto() + "; Custo médio = " + custoMedio() + ")");
            geracao += 1;
        }
        System.err.println("Feito.");
    }

    private List<Solucao> criaGeracao() {
        // avalia população
        var avaliacao = cfg.avaliador().avalia(pop, instancia);
        // gera indivíduos descendentes
        List<Solucao> descendentes = IntStream.range(0, cfg.tamanhoPopulacao()).parallel()
                .mapToObj((var i) -> {
                    var geradores = cfg.selecionador().seleciona(avaliacao, instancia);
                    return cfg.cruzador().cruza(geradores[0], geradores[1], instancia);
                })
                .collect(Collectors.toList());
        // muta descendentes
        if (cfg.mutador() != null)
            descendentes = cfg.mutador().muta(descendentes, instancia);
        // busca local
        if (cfg.buscaLocal() != null)
            descendentes = cfg.buscaLocal().buscaLocal(descendentes, instancia);
        // atualiza população com novos descendentes
        var novaPop = cfg.atualizador().atualiza(pop, descendentes, instancia);
        return novaPop;
    }

    public Solucao melhorSolucao() {
        if (pop.isEmpty())
            throw new RuntimeException("Tentativa de obter solução de problema não resolvido");
        return pop.stream().min((var a, var b) -> Double.compare(a.getCusto(), b.getCusto())).get();
    }

    public double custoMedio() {
        if (pop.isEmpty())
            throw new RuntimeException("Tentativa de obter média de custo de problema não resolvido");
        return pop.stream().collect(Collectors.averagingDouble((var l) -> l.getCusto()));

    }

    public boolean pronto() {
        return criterioDeParada();
    }

    private List<Solucao> geraPopulacaoInicial() {
        return cfg.gerador().geraPopulacaoInicial(instancia);
    }

    private boolean criterioDeParada() {
        return geracao >= cfg.maxGeracoes();
    }
}
