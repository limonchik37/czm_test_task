package fit.cvut.cz.exporter;


import java.time.Instant;
import java.util.List;

/** Final report with creation timestamp and statistic results. */
public record Report(Instant created, List<Result> statistics) { }