package fit.cvut.cz.exporter;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Report exporter contract (e.g., JSON, XML).
 */
public interface Exporter {
    /** Short format code (e.g., "JSON", "XML"). */
    String code();

    /**
     * Write the report to a file.
     * @param report data to write
     * @param out target path (parent directory should exist)
     */
    void export(Report report, Path out) throws IOException;
}