package fit.cvut.cz.facade;


import fit.cvut.cz.exporter.Report;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface ReportFacade {
    void setCsv(Path csvPath);
    void setStatCodes(List<String> codes);
    void setExportCodes(List<String> codes);
    void setOutDir(Path outDir);

    Report compute() throws IOException;
    List<Path> export(Report report) throws IOException;

    Map<String,String> availableStatistics();
    List<String> availableExporters();
}