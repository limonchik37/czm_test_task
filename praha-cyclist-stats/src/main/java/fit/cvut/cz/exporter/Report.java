package fit.cvut.cz.exporter;


import java.time.Instant;
import java.util.List;

public record Report(Instant created, List<Result> statistics) {

}