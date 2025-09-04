package fit.cvut.cz.statistics.implementations;

import fit.cvut.cz.measurement.Measurement;
import fit.cvut.cz.statistics.Statistics;

import java.util.List;

/**
 * NH — most popular hour of day (UTC).
 * Tie-break: lowest hour wins.
 */
public final class PopularHourStatistic implements Statistics {
    @Override
    public String code() { return "NH"; }
    @Override
    public String name() { return "Most popular hour of day"; }

    /**
     * @return hour (0–23) as string
     */
    @Override
    public String compute(List<Measurement> data) {
        long[] hours = new long[24];
        for (Measurement m : data) {
            int h = m.hourUTC();
            hours[h] += m.value();
        }

        int best = 0;
        for (int i = 1; i < 24; i++) {
            if (hours[i] > hours[best]) best = i;
        }
        return Integer.toString(best);
    }
}