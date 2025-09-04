package fit.cvut.cz.console.pages.response;

import fit.cvut.cz.console.TextPage;

import java.util.List;
import java.util.Map;

public record ListAvailablePage(Map<String,String> stats, List<String> exporters) implements TextPage {
    @Override public String load() {
        StringBuilder sb = new StringBuilder();
        sb.append("Statistics:\n");
        stats.forEach((c,n) -> sb.append("  ").append(c).append(" : ").append(n).append('\n'));
        sb.append("Exporters:\n");
        for (String e : exporters) sb.append("  ").append(e).append('\n');
        return sb.toString();
    }
}