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
        parser.addArgument("--semente")
                .type(Long.class)
                .help("A semente a ser utilizada pelo solucionador.");

        var ns = parser.parseArgsOrFail(args);

        var entrada = (File) ns.get("entrada");
        long semente;
        {
            var sementeArg = (Long) ns.get("semente");
            semente = sementeArg == null ? System.currentTimeMillis() : sementeArg;
        }

        String fonte;
        try {
            fonte = new String(Files.readAllBytes(entrada.toPath()), Charset.defaultCharset());
        } catch (IOException e) {
            // não vai acontecer
            return;
        }

        var instancia = Parser.parseProblema(entrada.toPath().getFileName().toString(), fonte);
        var resolvedor = new Resolvedor(instancia, semente);
        resolvedor.resolve();
        System.out.println(resolvedor.melhorSolucao().removeRedundantes());
    }
}
