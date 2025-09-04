package fit.cvut.cz.statistics.implementations;

import fit.cvut.cz.measurement.Measurement;
import fit.cvut.cz.measurement.Segment;
import fit.cvut.cz.statistics.Statistics;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * NU — most frequent segment (sum of values per Segment).
 * Tie-break: lexicographic by fromId, then toId.
 */
public final class PopularSegmentStatistic implements Statistics {
    @Override public String code() { return "NU"; }
    @Override public String name() { return "Most frequent segment"; }

    /**
     * @return "from->to" of the top segment, or "N/A" if none
     */
    @Override
    public String compute(List<Measurement> data) {
        Map<Segment, Long> perSeg = data.stream().collect(
                Collectors.groupingBy(Measurement::segment, Collectors.summingLong(Measurement::value))
        );
        return perSeg.entrySet().stream()
                .max(Comparator
                        .<Map.Entry<Segment, Long>>comparingLong(Map.Entry::getValue)
                        .thenComparing(e -> e.getKey().fromId())
                        .thenComparing(e -> e.getKey().toId())
                )
                .map(e -> e.getKey().fromId() + "->" + e.getKey().toId())
                .orElse("N/A");
    }
}