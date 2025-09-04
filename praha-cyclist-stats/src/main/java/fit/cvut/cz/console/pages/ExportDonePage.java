package fit.cvut.cz.console.pages;

import fit.cvut.cz.console.TextPage;

import java.nio.file.Path;
import java.util.List;

/**
 * Lists files produced by export.
 */
public record ExportDonePage(List<Path> files) implements TextPage {
    @Override
    public String load() {
        StringBuilder sb = new StringBuilder("Exported:\n");
        for (Path p : files) sb.append("  ").append(p).append('\n');
        return sb.toString();
    }
}