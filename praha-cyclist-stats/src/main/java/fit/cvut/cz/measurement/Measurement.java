package fit.cvut.cz.measurement;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * Single measurement item within a time interval.
 */
public interface Measurement {
    Segment segment();
    Instant measuredFrom();
    Instant measuredTo();
    int value();

    /** UTC calendar date derived from {@code measuredFrom}. */
    default LocalDate dateUTC() {
        return measuredFrom().atZone(ZoneOffset.UTC).toLocalDate();
    }
    /** UTC hour (0–23) derived from {@code measuredFrom}. */
    default int hourUTC() {
        return measuredFrom().atZone(ZoneOffset.UTC).getHour();
    }
}