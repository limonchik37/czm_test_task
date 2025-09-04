package fit.cvut.cz.console.pages;

import fit.cvut.cz.console.TextPage;

/**
 * One-line welcome message.
 */
public final class StartPage implements TextPage {
    @Override
    public String load() {
        return "Cyclists CLI — type --help for commands.\n";
    }
}