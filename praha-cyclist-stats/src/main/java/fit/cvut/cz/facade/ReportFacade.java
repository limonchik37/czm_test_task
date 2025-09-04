package fit.cvut.cz.facade;


import fit.cvut.cz.exporter.Report;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * High-level API to compute and export a report.
 */
public interface ReportFacade {
    void setCsv(Path csvPath);
    void setStatCodes(List<String> codes);
    void setExportCodes(List<String> codes);
    void setOutDir(Path outDir);

    /** Compute report using current configuration. */
    Report compute() throws IOException;

    /** Export report to selected formats; returns paths of created files. */
    List<Path> export(Report report) throws IOException;

    /** Available statistics: code -> name. */
    Map<String,String> availableStatistics();

    /** Available exporters (codes). */
    List<String> availableExporters();
}