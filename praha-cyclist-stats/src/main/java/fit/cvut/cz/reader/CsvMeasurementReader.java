package fit.cvut.cz.reader;

import fit.cvut.cz.measurement.CsvMeasurement;
import fit.cvut.cz.measurement.Measurement;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import fit.cvut.cz.measurement.Segment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * CSV reader (default delimiter: ',').
 * Expected header: locations_id,directions_id,measured_from,measured_to,value
 * Empty/invalid value → 0; invalid timestamps → row skipped.
 */
public final class CsvMeasurementReader implements MeasurementReader {
    private static final char SEP = ',';

    @Override
    public List<Measurement> read(Path path) throws IOException {
        try (var br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            var parser  = new CSVParserBuilder().withSeparator(SEP).build();
            try (CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build()) {

                if (reader.readNext() == null) return List.of();

                List<Measurement> out = new ArrayList<>();
                String[] row;
                while ((row = reader.readNext()) != null) {
                    if (row.length < 5) continue;

                    String loc  = safeTrim(row[0]);
                    String dir  = safeTrim(row[1]);
                    String from = safeTrim(row[2]);
                    String to   = safeTrim(row[3]);
                    String valS = safeTrim(row[4]);

                    Instant fromTs = parseInstant(from);
                    Instant toTs   = parseInstant(to);
                    if (fromTs == null || toTs == null) continue;

                    int value = parseIntOrZero(valS);

                    out.add(new CsvMeasurement(new Segment(loc, dir), fromTs, toTs, value));
                }
                return Collections.unmodifiableList(out);
            }
        } catch (CsvValidationException e) {
            throw new IOException("CSV validation error: " + path, e);
        }
    }

    // --- helpers ---
    /** Null/blank safe trim. */
    private static String safeTrim(String s) { return s == null ? "" : s.trim(); }

    /** Parse int or return 0 for blank/invalid. */
    private static int parseIntOrZero(String s) {
        if (s == null || s.isBlank()) return 0;
        try { return Integer.parseInt(s); } catch (Exception ignore) { return 0; }
    }

    /** Lenient ISO-8601 parse (allows space instead of 'T'). */
    private static Instant parseInstant(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Instant.parse(s);
        } catch (Exception e) {
            if (s.contains(" ") && !s.contains("T")) {
                try { return Instant.parse(s.replace(' ', 'T')); } catch (Exception ignore) {}
            }
            return null;
        }
    }
}