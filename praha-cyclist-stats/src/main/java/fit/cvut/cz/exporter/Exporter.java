package fit.cvut.cz.exporter;

import java.io.IOException;
import java.nio.file.Path;

public interface Exporter {
    String code();                        // "JSON", "XML"
    void export(Report report, Path out) throws IOException;
}