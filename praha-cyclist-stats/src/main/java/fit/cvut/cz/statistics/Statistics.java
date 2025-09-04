package fit.cvut.cz.statistics;

import fit.cvut.cz.measurement.Measurement;

import java.util.List;

/**
 * Contract for a single statistic (e.g., PP, DM, NH, NU).
 * Implementations should be pure (no side effects).
 */
public interface Statistics {
    /** Short code, e.g., "PP", "DM", "NH", "NU". */
    String code();

    /** Human-readable name shown in reports. */
    String name();

    /**
     * Compute statistic value over the dataset.
     * @param data measurements to process
     * @return display value (formatted string)
     */
    String compute(List<Measurement> data);
}