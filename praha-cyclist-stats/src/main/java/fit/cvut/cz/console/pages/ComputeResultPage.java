package fit.cvut.cz.console.pages;

import fit.cvut.cz.console.TextPage;
import fit.cvut.cz.exporter.Report;
import fit.cvut.cz.exporter.Result;

/**
 * Prints computed results in a readable block.
 */
public record ComputeResultPage(Report report) implements TextPage {
    @Override
    public String load() {
        StringBuilder sb = new StringBuilder();
        sb.append("Created: ").append(report.created()).append('\n');
        for (Result r : report.statistics()) {
            sb.append("  ").append(r.name()).append(" = ").append(r.result()).append('\n');
        }
        return sb.toString();
    }
}