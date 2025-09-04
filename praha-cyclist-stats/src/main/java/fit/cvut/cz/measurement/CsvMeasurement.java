package fit.cvut.cz.measurement;

import java.time.Instant;
import java.util.Objects;

public record CsvMeasurement(
        Segment segment,
        Instant measuredFrom,
        Instant measuredTo,
        int value
) implements Measurement {
    public CsvMeasurement {
        Objects.requireNonNull(segment, "segment");
        Objects.requireNonNull(measuredFrom, "measuredFrom");
        Objects.requireNonNull(measuredTo, "measuredTo");
        if (value < 0) value = 0;
    }

    public static CsvMeasurement of(String fromId, String toId, Instant from, Instant to, String valueStr) {
        int v = 0;
        if (valueStr != null && !valueStr.isBlank()) {
            try { v = Integer.parseInt(valueStr.trim()); } catch (NumberFormatException ignored) {}
        }
        return new CsvMeasurement(new Segment(
                fromId == null ? "" : fromId.trim(),
                toId   == null ? "" : toId.trim()
        ), from, to, Math.max(0, v));
    }
}