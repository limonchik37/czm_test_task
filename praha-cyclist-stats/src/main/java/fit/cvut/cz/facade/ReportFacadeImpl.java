package fit.cvut.cz.facade;

import fit.cvut.cz.exporter.Exporter;
import fit.cvut.cz.exporter.Report;
import fit.cvut.cz.exporter.Result;
import fit.cvut.cz.measurement.Measurement;
import fit.cvut.cz.reader.MeasurementReader;
import fit.cvut.cz.statistics.Statistics;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Facade that wires reader, statistics, and exporters.
 * Holds runtime config (CSV path, selected stats/exporters, output dir).
 */
public final class ReportFacadeImpl implements ReportFacade {

    private final MeasurementReader reader;
    private final Map<String, Statistics> statsByCode;
    private final Map<String, Exporter> exportersByCode;

    private Path csvPath;
    private List<String> statCodes = List.of();
    private List<String> exportCodes = List.of();
    private Path outDir = Path.of(".");

    /**
     * @param reader CSV -> measurements
     * @param stats registered statistics (by code)
     * @param exporters registered exporters (by code)
     */
    public ReportFacadeImpl(MeasurementReader reader,
                            Collection<Statistics> stats,
                            Collection<Exporter> exporters) {
        this.reader = Objects.requireNonNull(reader);
        this.statsByCode = stats.stream().collect(Collectors.toUnmodifiableMap(
                s -> s.code().toUpperCase(Locale.ROOT), s -> s));
        this.exportersByCode = exporters.stream().collect(Collectors.toUnmodifiableMap(
                e -> e.code().toUpperCase(Locale.ROOT), e -> e));
    }

    @Override
    public void setCsv(Path csvPath) { this.csvPath = csvPath; }
    @Override
    public void setStatCodes(List<String> codes) { this.statCodes = norm(codes); }
    @Override
    public void setExportCodes(List<String> codes) { this.exportCodes = norm(codes); }
    @Override
    public void setOutDir(Path outDir) { this.outDir = outDir; }

    /**
     * Reads CSV and computes selected statistics.
     * @return report with timestamp and results
     * @throws IOException on read/parse errors
     * @throws IllegalStateException if CSV path is not set
     */
    @Override
    public Report compute() throws IOException {
        if (csvPath == null) throw new IllegalStateException("CSV path not set");
        List<Measurement> data = reader.read(csvPath);

        List<Result> results = new ArrayList<>();
        for (String code : statCodes) {
            Statistics s = statsByCode.get(code);
            if (s == null) continue;
            results.add(new Result(s.name(), s.compute(data)));
        }
        return new Report(Instant.now(), results);
    }

    /**
     * Exports the report using the selected exporters.
     * @return list of written file paths
     * @throws IOException on write errors
     */
    @Override
    public List<Path> export(Report report) throws IOException {
        List<Path> written = new ArrayList<>();
        for (String code : exportCodes) {
            Exporter ex = exportersByCode.get(code);
            if (ex == null) continue;
            Path out = outDir.resolve("export." + ex.code().toLowerCase(Locale.ROOT));
            // Ensure parent directory exists if needed.
            ex.export(report, out);
            written.add(out);
        }
        return written;
    }

    /** @return available statistics: code -> human name */
    @Override
    public Map<String, String> availableStatistics() {
        return statsByCode.values().stream()
                .collect(Collectors.toMap(Statistics::code, Statistics::name));
    }

    /** @return available exporter codes (e.g., JSON, XML) */
    @Override
    public List<String> availableExporters() {
        return exportersByCode.keySet().stream().sorted().toList();
    }

    /** Normalize codes: trim + upper + distinct, drop null/blank. */
    private static List<String> norm(List<String> codes) {
        return codes.stream().filter(Objects::nonNull)
                .map(s -> s.trim().toUpperCase(Locale.ROOT))
                .filter(s -> !s.isEmpty())
                .distinct().toList();
    }
}