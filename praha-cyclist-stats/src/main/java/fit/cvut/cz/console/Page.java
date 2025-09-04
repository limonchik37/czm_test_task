package fit.cvut.cz.console;

/**
 * Minimal page contract.
 * @param <T> render result type
 */
public interface Page<T> {
    /** Render the page content. */
    T load();
}