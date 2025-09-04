package fit.cvut.cz.console.pages;

import fit.cvut.cz.console.TextPage;

import java.nio.file.Path;

/**
 * Confirms output directory selection.
 */
public record OutDirSetPage(Path dir) implements TextPage {
    @Override
    public String load() { return "Output dir: " + dir + "\n"; }
}