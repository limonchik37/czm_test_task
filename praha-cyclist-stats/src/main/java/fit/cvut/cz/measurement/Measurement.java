package fit.cvut.cz.measurement;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public interface Measurement {
    Segment segment();
    Instant measuredFrom();
    Instant measuredTo();
    int value();

    default LocalDate dateUTC() {
        return measuredFrom().atZone(ZoneOffset.UTC).toLocalDate();
    }
    default int hourUTC() {
        return measuredFrom().atZone(ZoneOffset.UTC).getHour();
    }
}
