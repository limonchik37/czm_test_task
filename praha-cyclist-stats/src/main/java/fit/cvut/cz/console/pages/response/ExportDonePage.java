package fit.cvut.cz.console.pages.response;

import fit.cvut.cz.console.TextPage;

import java.nio.file.Path;
import java.util.List;

public record ExportDonePage(List<Path> files) implements TextPage {

    @Override public String load() {
        StringBuilder sb = new StringBuilder("Exported:\n");
        for (Path p : files) sb.append("  ").append(p).append('\n');
        return sb.toString();
    }
}