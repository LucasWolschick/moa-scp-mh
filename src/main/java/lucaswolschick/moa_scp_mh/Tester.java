package lucaswolschick.moa_scp_mh;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import lucaswolschick.moa_scp_mh.parser.Instancia;
import lucaswolschick.moa_scp_mh.parser.Parser;
import lucaswolschick.moa_scp_mh.resolvedor.Gerador;
import lucaswolschick.moa_scp_mh.resolvedor.Resolvedor;
import lucaswolschick.moa_scp_mh.resolvedor.ResolvedorConfiguracao;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores.Atualizacao;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores.Avaliacao;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores.Cruzamento;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores.Mutacao;
import lucaswolschick.moa_scp_mh.resolvedor.Operadores.Selecao;
import lucaswolschick.moa_scp_mh.resolvedor.atualizadores.AtualizadorElitista;
import lucaswolschick.moa_scp_mh.resolvedor.avaliadores.AvaliadorCusto;
import lucaswolschick.moa_scp_mh.resolvedor.buscaLocal.BuscaLocal;
import lucaswolschick.moa_scp_mh.resolvedor.cruzadores.CruzadorRemovedorRedundancias;
import lucaswolschick.moa_scp_mh.resolvedor.geradores.GeradorAleatorio;
import lucaswolschick.moa_scp_mh.resolvedor.mutadores.MutadorTrocaColunasAleatorio;
import lucaswolschick.moa_scp_mh.resolvedor.selecionadores.SelecionadorClassificador;
import lucaswolschick.moa_scp_mh.resolvedor.selecionadores.SelecionadorTorneio;

/*
 * implementar a questão das sementes.

    Tamanho da população: 100, 1000;
    Número de gerações: 10, 20;
    Avaliador: de custo;
    Selecionador: por ordenação e por torneio (k = 3);
    Cruzador: por remoção de redundâncias;
    Mutador: por substituição de colunas (p = 0.2);
    Busca local: por first improvement (n = 2) e best improvement (n=1);
    Atualizador: elitista (f = 0.15).
 */

public class Tester {
    public static void main(String[] args) {
        int[] tamPop = { 100, 1000 };
        int[] numGer = { 10, 20 };
        Instancia[] instancias = Arrays
                .stream(new String[] { "Teste_01.dat", "Teste_02.dat", "Teste_03.dat", "Teste_04.dat", "Wren_01.dat",
                        "Wren_02.dat", "Wren_03.dat", "Wren_04.dat", })
                .map((s) -> Parser.parseFile(Path.of("input", s))).toArray(Instancia[]::new);
        Gerador[] geradores = { new GeradorAleatorio() };
        Avaliacao[] avaliadores = { new AvaliadorCusto() };
        Selecao[] selecionadores = { new SelecionadorClassificador(), new SelecionadorTorneio(3) };
        Cruzamento[] cruzadores = { new CruzadorRemovedorRedundancias() };
        Mutacao[] mutadores = { new MutadorTrocaColunasAleatorio(0.2) };
        BuscaLocal[] buscaLocals = { new BuscaLocal(BuscaLocal.Estrategia.FIRST_IMPROVEMENT, 2),
                new BuscaLocal(BuscaLocal.Estrategia.BEST_IMPROVEMENT, 1) };
        Atualizacao[] atualizadores = { new AtualizadorElitista(0.15) };

        int semente = 42;

        List<Resolvedor> resolvedores = new ArrayList<>();

        for (int tp : tamPop) {
            for (int ng : numGer) {
                for (Instancia inst : instancias) {
                    for (Gerador ger : geradores) {
                        for (Avaliacao ava : avaliadores) {
                            for (Selecao sel : selecionadores) {
                                for (Cruzamento cruz : cruzadores) {
                                    for (Mutacao mut : mutadores) {
                                        for (BuscaLocal bl : buscaLocals) {
                                            for (Atualizacao atu : atualizadores) {
                                                resolvedores.add(new Resolvedor(inst, semente,
                                                        new ResolvedorConfiguracao(tp, ng, ger, ava, sel, cruz, mut, bl,
                                                                atu)));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        var out = System.out;
        IntStream.range(0, resolvedores.size())
                .forEach((i) -> {
                    var resolvedor = resolvedores.get(i);
                    System.err.println("Resolvendo " + (i + 1) + "/" + resolvedores.size() + "...");
                    System.err.println(resolvedor.cfg());
                    try {
                        var newOut = new File("output/" + i + ".txt");
                        newOut.createNewFile();
                        var printStream = new PrintStream(newOut);
                        System.setOut(printStream);
                        resolvedor.resolve();
                        System.out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        System.setOut(out);
    }
}
