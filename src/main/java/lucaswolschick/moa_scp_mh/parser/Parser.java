package lucaswolschick.moa_scp_mh.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

import java.nio.file.Path;

public class Parser {
    private static List<Double> extraiNumeros(String s) {
        var p = Pattern.compile("[-+]?(?:\\d*\\.*\\d+)");
        var m = p.matcher(s);
        List<Double> lst = new ArrayList<>();
        while (m.find()) {
            lst.add(Double.parseDouble(m.group()));
        }
        return lst;
    }

    public static Instancia parseProblema(String nome, String src) {
        var linhas = src.lines()
                .map(String::trim)
                .filter((s) -> !s.isEmpty())
                .collect(toList());
        var n_linhas = (int) Math.round(extraiNumeros(linhas.get(0)).get(0));
        var n_colunas = (int) Math.round(extraiNumeros(linhas.get(1)).get(0));
        var dados = linhas.stream().skip(3).map(Coluna::parseColuna).collect(toList());

        var total = dados.stream()
                .collect(Collectors.summingInt((d) -> d.elem().size()));
        var densidade = (double) total / (n_linhas * n_colunas);

        return new Instancia(nome, n_linhas, n_colunas, densidade, dados);
    }

    public static Instancia parseFile(Path path) {
        try {
            var nome = path.getFileName().toString();
            var src = new String(java.nio.file.Files.readAllBytes(path));
            return parseProblema(nome, src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
