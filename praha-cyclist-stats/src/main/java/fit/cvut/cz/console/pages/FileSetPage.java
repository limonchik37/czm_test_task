package fit.cvut.cz.console.pages;

import fit.cvut.cz.console.TextPage;

import java.nio.file.Path;

/**
 * Confirms CSV file selection.
 */
public record FileSetPage(Path path) implements TextPage {
    @Override
    public String load() { return "CSV set: " + path + "\n"; }
}