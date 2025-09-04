package fit.cvut.cz.exporter.implementation;

import fit.cvut.cz.exporter.Exporter;
import fit.cvut.cz.exporter.Report;
import fit.cvut.cz.exporter.Result;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JsonExporter implements Exporter {
    @Override public String code() { return "JSON"; }

    @Override
    public void export(Report report, Path out) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"created\": \"").append(report.created()).append("\",\n");
        sb.append("  \"statistics\": [\n");
        for (int i = 0; i < report.statistics().size(); i++) {
            Result r = report.statistics().get(i);
            sb.append("    { \"name\": \"").append(r.name())
                    .append("\", \"result\": \"").append(r.result()).append("\" }");
            if (i < report.statistics().size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n");
        sb.append("}\n");
        Files.writeString(out, sb.toString(), StandardCharsets.UTF_8);
    }
}