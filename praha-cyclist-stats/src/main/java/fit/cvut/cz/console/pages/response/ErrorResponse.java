package fit.cvut.cz.console.pages.response;

import fit.cvut.cz.console.TextPage;

/**
 * One-line error page prefixing the message with "ERROR:".
 */
public record ErrorResponse(String message) implements TextPage {
    @Override
    public String load() { return "ERROR: " + message + "\n"; }
}