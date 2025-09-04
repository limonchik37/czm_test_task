package fit.cvut.cz.statistics;

import fit.cvut.cz.measurement.Measurement;

import java.util.List;

public interface Statistics {
    String code();
    String name();
    String compute(List<Measurement> data);
}