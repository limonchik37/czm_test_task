package fit.cvut.cz.measurement;

import java.time.Instant;
import java.util.Objects;

/**
 * CSV-backed measurement; negative values are clamped to 0.
 */
public record CsvMeasurement(
        Segment segment,
        Instant measuredFrom,
        Instant measuredTo,
        int value
) implements Measurement {

    /** Canonical ctor with null checks and non-negative value. */
    public CsvMeasurement {
        Objects.requireNonNull(segment, "segment");
        Objects.requireNonNull(measuredFrom, "measuredFrom");
        Objects.requireNonNull(measuredTo, "measuredTo");
        if (value < 0) value = 0;
    }

    /**
     * Factory with safe parsing of the value field.
     * @param fromId source segment id (trimmed, null → "")
     * @param toId target segment id (trimmed, null → "")
     * @param from start timestamp (required)
     * @param to end timestamp (required)
     * @param valueStr numeric string; blank/invalid → 0
     */
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