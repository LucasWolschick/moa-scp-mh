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

    public Resolvedor(Instancia instancia, long semente) {
        this(instancia, new ResolvedorConfiguracao(
                100,
                10,
                semente,
                new GeradorAleatorio(),
                new AvaliadorCusto(),
                new SelecionadorTorneio(3),
                new CruzadorRemovedorRedundancias(),
                new MutadorTrocaColunasAleatorio(0.2f),
                new BuscaLocal(BuscaLocal.Estrategia.BEST_IMPROVEMENT, 1),
                new AtualizadorElitista(0.15f)));
    }

    public void resolve() {
        long semente = this.cfg.semente();

        System.out.println("Instância: " + instancia.nome());
        System.out.println("Configuração: " + cfg);
        System.out.println("Semente: " + semente);
        System.out.println("Gerando população inicial (geração 1)...");

        var now = System.currentTimeMillis();

        pop = geraPopulacaoInicial();
        System.out.println(
                "(Custo = " + melhorSolucao().getCusto() + "; Custo médio = " + custoMedio() + "; Desvio padrão = "
                        + desvioPadraoCusto() + "; tempo = " + (System.currentTimeMillis() - now) / 1000.0 + "s)");
        geracao += 1;

        while (!criterioDeParada()) {
            System.out.println("Geração " + (geracao + 1) + "...");
            pop = criaGeracao();
            System.out.println(
                    "(Custo = " + melhorSolucao().getCusto() + "; Custo médio = " + custoMedio() + "; Desvio padrão = "
                            + desvioPadraoCusto() + "; tempo = " + (System.currentTimeMillis() - now) / 1000.0 + "s)");
            geracao += 1;
        }
        System.out.println("Feito.");

        var elapsed = System.currentTimeMillis() - now;

        System.out.println("Tempo de execução: " + elapsed / 1000.0 + "s");
    }

    private List<Solucao> criaGeracao() {
        long semente = this.cfg.semente();

        // avalia população
        var avaliacao = cfg.avaliador().avalia(pop, instancia, semente);
        // gera indivíduos descendentes
        List<Solucao> descendentes = IntStream.range(0, cfg.tamanhoPopulacao()).parallel()
                .mapToObj((var i) -> {
                    var geradores = cfg.selecionador().seleciona(avaliacao, instancia,
                            semente + i + geracao * cfg.tamanhoPopulacao());
                    return cfg.cruzador().cruza(geradores[0], geradores[1], instancia,
                            semente + i + geracao * cfg.tamanhoPopulacao());
                })
                .collect(Collectors.toList());
        // muta descendentes
        if (cfg.mutador() != null)
            descendentes = cfg.mutador().muta(descendentes, instancia, semente + geracao * cfg.tamanhoPopulacao());
        // busca local
        if (cfg.buscaLocal() != null)
            descendentes = cfg.buscaLocal().buscaLocal(descendentes, instancia,
                    semente + geracao * cfg.tamanhoPopulacao());
        // atualiza população com novos descendentes
        var novaPop = cfg.atualizador().atualiza(pop, descendentes, cfg.tamanhoPopulacao(), instancia,
                semente + geracao * cfg.tamanhoPopulacao());
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

    public double desvioPadraoCusto() {
        if (pop.isEmpty())
            throw new RuntimeException("Tentativa de obter desvio padrão de custo de problema não resolvido");
        var media = custoMedio();
        return Math.sqrt(pop.stream().map((var l) -> Math.pow(l.getCusto() - media, 2))
                .collect(Collectors.summingDouble((var l) -> l)) / (pop.size() - 1));
    }

    public boolean pronto() {
        return criterioDeParada();
    }

    private List<Solucao> geraPopulacaoInicial() {
        long semente = this.cfg.semente();
        return cfg.gerador().geraPopulacaoInicial(instancia, cfg.tamanhoPopulacao(), semente);
    }

    private boolean criterioDeParada() {
        return geracao >= cfg.maxGeracoes();
    }

    public ResolvedorConfiguracao cfg() {
        return cfg;
    }
}
