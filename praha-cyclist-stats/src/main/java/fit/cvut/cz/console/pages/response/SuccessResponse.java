package fit.cvut.cz.console.pages.response;

import fit.cvut.cz.console.TextPage;

/**
 * One-line success page prefixing the message with "OK:".
 */
public record SuccessResponse(String message) implements TextPage {
    @Override
    public String load() { return "OK: " + message + "\n"; }
}