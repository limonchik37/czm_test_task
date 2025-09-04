package fit.cvut.cz.reader;

import fit.cvut.cz.measurement.Measurement;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Reads cyclist measurements from a CSV file into domain objects.
 */
public interface MeasurementReader {
    /**
     * Parse a CSV file into a list of measurements.
     * @param path path to CSV
     * @return immutable list of measurements (may be empty)
     * @throws IOException I/O or CSV parse error
     */
    List<Measurement> read(Path path) throws IOException;
}