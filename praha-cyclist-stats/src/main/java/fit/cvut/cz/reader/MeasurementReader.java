package fit.cvut.cz.reader;

import fit.cvut.cz.measurement.Measurement;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface MeasurementReader {
    List<Measurement> read(Path path) throws IOException;
}