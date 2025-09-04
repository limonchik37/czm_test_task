package fit.cvut.cz.console.pages.response;

import fit.cvut.cz.console.TextPage;

import java.util.List;

public record ExportSetPage(List<String> codes) implements TextPage {
    @Override
    public String load() { return "Export set: " + String.join(",", codes) + "\n"; }
}