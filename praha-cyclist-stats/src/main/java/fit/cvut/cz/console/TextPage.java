package fit.cvut.cz.console;

/**
 * A page that renders to plain text for console output.
 */
public interface TextPage extends Page<String> {
    @Override
    String load();
}