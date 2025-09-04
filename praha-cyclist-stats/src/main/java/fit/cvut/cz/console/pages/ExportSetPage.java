package fit.cvut.cz.console.pages;

import fit.cvut.cz.console.TextPage;

import java.util.List;

/**
 * Shows selected exporter codes.
 */
public record ExportSetPage(List<String> codes) implements TextPage {
    @Override
    public String load() { return "Export set: " + String.join(",", codes) + "\n"; }
}