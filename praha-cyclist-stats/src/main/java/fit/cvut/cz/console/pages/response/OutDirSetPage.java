package fit.cvut.cz.console.pages.response;

import fit.cvut.cz.console.TextPage;

import java.nio.file.Path;

public record OutDirSetPage(Path dir) implements TextPage {
    @Override
    public String load() { return "Output dir: " + dir + "\n"; }
}