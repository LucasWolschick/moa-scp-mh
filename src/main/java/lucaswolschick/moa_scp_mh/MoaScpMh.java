package lucaswolschick.moa_scp_mh;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import lucaswolschick.moa_scp_mh.parser.Parser;
import lucaswolschick.moa_scp_mh.resolvedor.Gerador;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores;
import lucaswolschick.moa_scp_mh.resolvedor.Resolvedor;
import lucaswolschick.moa_scp_mh.resolvedor.ResolvedorConfiguracao;
import lucaswolschick.moa_scp_mh.resolvedor.atualizadores.AtualizadorElitista;
import lucaswolschick.moa_scp_mh.resolvedor.avaliadores.AvaliadorCusto;
import lucaswolschick.moa_scp_mh.resolvedor.buscaLocal.BuscaLocal;
import lucaswolschick.moa_scp_mh.resolvedor.cruzadores.CruzadorRemovedorRedundancias;
import lucaswolschick.moa_scp_mh.resolvedor.geradores.GeradorAleatorio;
import lucaswolschick.moa_scp_mh.resolvedor.mutadores.MutadorTrocaColunasAleatorio;
import lucaswolschick.moa_scp_mh.resolvedor.mutadores.MutadorTrocaUmaColunaAleatorio;
import lucaswolschick.moa_scp_mh.resolvedor.selecionadores.SelecionadorClassificador;
import lucaswolschick.moa_scp_mh.resolvedor.selecionadores.SelecionadorTorneio;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;

public class MoaScpMh {

    public static void main(String[] args) {
        var parser = ArgumentParsers.newFor("MOA-SCP-MH").build()
                .defaultHelp(true)
                .description(
                        "Obtém uma solução aproximada para o problema de cobertura de conjuntos usando Algoritmos Genéticos.");
        parser.addArgument("entrada")
                .type(Arguments.fileType().verifyExists().verifyIsFile().verifyCanRead().acceptSystemIn())
                .help("O problema a ser resolvido.");
        parser.addArgument("--semente")
                .type(Long.class)
                .setDefault(System.currentTimeMillis())
                .help("A semente a ser utilizada pelo solucionador.");

        // parâmetros e operadores do resolvedor
        parser.addArgument("--tamPop", "-p")
                .type(Integer.class)
                .setDefault(100)
                .help("O tamanho da população.");
        parser.addArgument("--maxGer", "-g")
                .type(Integer.class)
                .setDefault(10)
                .help("O número máximo de gerações.");
        parser.addArgument("--gerador")
                .type(String.class)
                .choices("aleatorio")
                .setDefault("aleatorio")
                .help("O gerador de soluções iniciais.");
        parser.addArgument("--avaliador")
                .type(String.class)
                .choices("custo")
                .setDefault("custo")
                .help("O avaliador de soluções.");
        parser.addArgument("--selecionador")
                .type(String.class)
                .choices("torneio", "classificacao")
                .setDefault("torneio")
                .help("O selecionador de soluções.");
        parser.addArgument("--cruzador")
                .type(String.class)
                .choices("removerRedundancias")
                .setDefault("removerRedundancias")
                .help("O cruzador de soluções.");
        parser.addArgument("--mutador")
                .type(String.class)
                .choices("trocaColunasAleatorio", "trocaUmaColunaAleatorio")
                .setDefault("trocaColunasAleatorio")
                .help("O mutador de soluções.");
        parser.addArgument("--buscaLocal")
                .type(String.class)
                .choices("bestImprovement", "firstImprovement")
                .help("A busca local a ser utilizada.");
        parser.addArgument("--atualizador")
                .type(String.class)
                .choices("elitista")
                .setDefault("elitista")
                .help("O atualizador de população.");

        var ns = parser.parseArgsOrFail(args);

        var entrada = (File) ns.get("entrada");
        String fonte;
        try {
            fonte = new String(Files.readAllBytes(entrada.toPath()), Charset.defaultCharset());
        } catch (IOException e) {
            // não vai acontecer
            return;
        }

        long semente = (Long) ns.get("semente");
        int tamanhoPopulacao = (Integer) ns.get("tamPop");
        int maxGeracoes = (Integer) ns.get("maxGer");
        Gerador gerador;
        switch ((String) ns.get("gerador")) {
            case "aleatorio":
                gerador = new GeradorAleatorio();
                break;
            default:
                throw new RuntimeException("Gerador não reconhecido");
        }
        Operadores.Avaliacao avaliador;
        switch ((String) ns.get("avaliador")) {
            case "custo":
                avaliador = new AvaliadorCusto();
                break;
            default:
                throw new RuntimeException("Avaliador não reconhecido");
        }
        Operadores.Selecao selecionador;
        switch ((String) ns.get("selecionador")) {
            case "torneio":
                selecionador = new SelecionadorTorneio(3);
                break;
            case "classificacao":
                selecionador = new SelecionadorClassificador();
                break;
            default:
                throw new RuntimeException("Selecionador não reconhecido");
        }
        Operadores.Cruzamento cruzador;
        switch ((String) ns.get("cruzador")) {
            case "removerRedundancias":
                cruzador = new CruzadorRemovedorRedundancias();
                break;
            default:
                throw new RuntimeException("Cruzador não reconhecido");
        }
        Operadores.Mutacao mutador;
        switch ((String) ns.get("mutador")) {
            case "trocaColunasAleatorio":
                mutador = new MutadorTrocaColunasAleatorio(0.20);
                break;
            case "trocaUmaColunaAleatorio":
                mutador = new MutadorTrocaUmaColunaAleatorio(0.15);
                break;
            default:
                throw new RuntimeException("Mutador não reconhecido");
        }
        Operadores.BuscaLocal buscaLocal = null;
        if ((String) ns.get("buscaLocal") != null) {
            switch ((String) ns.get("buscaLocal")) {
                case "bestImprovement":
                    buscaLocal = new BuscaLocal(BuscaLocal.Estrategia.BEST_IMPROVEMENT, 1);
                    break;
                case "firstImprovement":
                    buscaLocal = new BuscaLocal(BuscaLocal.Estrategia.FIRST_IMPROVEMENT, 1);
                    break;
                default:
                    buscaLocal = null;
            }
        }
        Operadores.Atualizacao atualizador;
        switch ((String) ns.get("atualizador")) {
            case "elitista":
                atualizador = new AtualizadorElitista(0.15);
                break;
            default:
                throw new RuntimeException("Atualizador não reconhecido");
        }

        var instancia = Parser.parseProblema(entrada.toPath().getFileName().toString(), fonte);
        var cfg = new ResolvedorConfiguracao(
                tamanhoPopulacao,
                maxGeracoes,
                semente,
                gerador,
                avaliador,
                selecionador,
                cruzador,
                mutador,
                buscaLocal,
                atualizador);
        var resolvedor = new Resolvedor(instancia, cfg);
        resolvedor.resolve();
        System.out.println(resolvedor.melhorSolucao().removeRedundantes());
    }
}
