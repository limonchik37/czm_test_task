package fit.cvut.cz.statistics.implementations;

import fit.cvut.cz.measurement.Measurement;
import fit.cvut.cz.statistics.Statistics;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DM — day with maximum total cyclist count.
 * Tie-break: earlier date.
 */
public final class DayMaxStatistic implements Statistics {
    @Override
    public String code() { return "DM"; }
    @Override
    public String name() { return "Day with maximum cyclist count"; }

    /**
     * @return date "yyyy-MM-dd" or "N/A" if dataset empty
     */
    @Override
    public String compute(List<Measurement> data) {
        Map<LocalDate, Long> perDay = data.stream().collect(
                Collectors.groupingBy(Measurement::dateUTC, Collectors.summingLong(Measurement::value))
        );
        return perDay.entrySet().stream()
                .max(Comparator
                        .<Map.Entry<LocalDate, Long>>comparingLong(Map.Entry::getValue)
                        .thenComparing(Map.Entry::getKey)
                )
                .map(e -> e.getKey().toString())
                .orElse("N/A");
    }
}