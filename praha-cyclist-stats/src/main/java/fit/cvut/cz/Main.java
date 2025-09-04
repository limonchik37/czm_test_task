package fit.cvut.cz;

import fit.cvut.cz.measurement.Measurement;
import fit.cvut.cz.reader.CsvMeasurementReader;
import fit.cvut.cz.reader.MeasurementReader;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Path path = resourcePath("bc-2020-q1.csv");

        MeasurementReader reader = new CsvMeasurementReader();
        List<Measurement> data = reader.read(path);

        System.out.println("Rows read: " + data.size());
        for (int i = 0; i < Math.min(10, data.size()); i++) {
            var m = data.get(i);
            System.out.printf("%s -> %s | from=%s | to=%s | val=%d | date=%s | hour=%d%n",
                    m.segment().fromId(), m.segment().toId(),
                    m.measuredFrom(), m.measuredTo(), m.value(),
                    m.dateUTC(), m.hourUTC());
        }
    }

    private static Path resourcePath(String fileName) throws URISyntaxException {
        var url = Main.class.getClassLoader().getResource(fileName);
        if (url == null) throw new IllegalArgumentException("Resource not found: " + fileName);
        return Paths.get(url.toURI());
    }
}