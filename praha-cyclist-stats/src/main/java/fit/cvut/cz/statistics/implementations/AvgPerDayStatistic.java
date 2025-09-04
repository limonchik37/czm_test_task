package fit.cvut.cz.statistics.implementations;

import fit.cvut.cz.measurement.Measurement;
import fit.cvut.cz.statistics.Statistics;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public final class AvgPerDayStatistic implements Statistics {
    @Override public String code() { return "PP"; }
    @Override public String name() { return "Average cyclist count per day"; }

    @Override
    public String compute(List<Measurement> data) {
        Map<LocalDate, Long> perDay = data.stream().collect(
                Collectors.groupingBy(Measurement::dateUTC, Collectors.summingLong(Measurement::value))
        );
        if (perDay.isEmpty()) return "0.00";
        double avg = perDay.values().stream().mapToLong(Long::longValue).average().orElse(0.0);
        return String.format(Locale.ROOT, "%.2f", avg);
    }
}