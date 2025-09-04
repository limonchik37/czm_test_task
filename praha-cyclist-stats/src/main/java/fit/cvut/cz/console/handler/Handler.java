package fit.cvut.cz.console.handler;

/**
 * Minimal contract for an interactive handler (e.g., CLI).
 */
public interface Handler {
    /**
     * Starts the interaction loop.
     * @throws Exception if the underlying implementation fails
     */
    void run() throws Exception;
}