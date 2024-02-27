package lucaswolschick.moa_scp_mh;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import lucaswolschick.moa_scp_mh.parser.Parser;
import lucaswolschick.moa_scp_mh.resolvedor.Resolvedor;
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

        var ns = parser.parseArgsOrFail(args);

        var entrada = (File) ns.get("entrada");

        String fonte;
        try {
            fonte = new String(Files.readAllBytes(entrada.toPath()), Charset.defaultCharset());
        } catch (IOException e) {
            // não vai acontecer
            return;
        }

        var instancia = Parser.parseProblema(fonte);
        var resolvedor = new Resolvedor(instancia, 100, 10);
        resolvedor.resolve();
        System.out.println(resolvedor.melhorSolucao().removeRedundantes());
    }
}
