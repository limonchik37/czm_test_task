package fit.cvut.cz.console.pages.response;

import fit.cvut.cz.console.TextPage;

import java.nio.file.Path;

public record FileSetPage(Path path) implements TextPage {
    @Override
    public String load() { return "CSV set: " + path + "\n"; }
}