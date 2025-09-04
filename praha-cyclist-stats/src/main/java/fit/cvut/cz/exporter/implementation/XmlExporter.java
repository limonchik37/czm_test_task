package fit.cvut.cz.exporter.implementation;

import fit.cvut.cz.exporter.Exporter;
import fit.cvut.cz.exporter.Report;
import fit.cvut.cz.exporter.Result;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class XmlExporter implements Exporter {
    @Override public String code() { return "XML"; }

    @Override
    public void export(Report report, Path out) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<report>\n");
        sb.append("  <created>").append(report.created()).append("</created>\n");
        sb.append("  <statisticsResults>\n");
        for (Result r : report.statistics()) {
            sb.append("    <statistics>\n");
            sb.append("      <name>").append(escape(r.name())).append("</name>\n");
            sb.append("      <result>").append(escape(r.result())).append("</result>\n");
            sb.append("    </statistics>\n");
        }
        sb.append("  </statisticsResults>\n");
        sb.append("</report>\n");
        Files.writeString(out, sb.toString(), StandardCharsets.UTF_8);
    }

    private static String escape(String s) {
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}