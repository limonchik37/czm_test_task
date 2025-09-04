package fit.cvut.cz;

import fit.cvut.cz.console.handler.ConsoleHandler;
import fit.cvut.cz.console.handler.Handler;
import fit.cvut.cz.exporter.implementation.JsonExporter;
import fit.cvut.cz.exporter.implementation.XmlExporter;
import fit.cvut.cz.facade.ReportFacade;
import fit.cvut.cz.facade.ReportFacadeImpl;
import fit.cvut.cz.reader.CsvMeasurementReader;
import fit.cvut.cz.reader.MeasurementReader;
import fit.cvut.cz.statistics.implementations.AvgPerDayStatistic;
import fit.cvut.cz.statistics.implementations.DayMaxStatistic;
import fit.cvut.cz.statistics.implementations.PopularHourStatistic;
import fit.cvut.cz.statistics.implementations.PopularSegmentStatistic;

import java.util.List;

/**
 * Entry point for the Cyclists CLI.
 * Wires reader + statistics + exporters into the facade and starts the console UI.
 */
public final class Main {

    /**
     * Starts the interactive console.
     * @param args unused (configuration is done interactively)
     */
    public static void main(String[] args) throws Exception {
        // 1) data source (CSV reader)
        MeasurementReader reader = new CsvMeasurementReader();

        // 2) statistics to compute
        var stats = List.of(
                new AvgPerDayStatistic(),   // PP
                new DayMaxStatistic(),      // DM
                new PopularHourStatistic(), // NH
                new PopularSegmentStatistic() // NU
        );

        // 3) output formats
        var exporters = List.of(
                new JsonExporter(),
                new XmlExporter()
        );

        // 4) facade (glues everything together)
        ReportFacade facade = new ReportFacadeImpl(reader, stats, exporters);

        // 5) console handler (interactive shell)
        Handler handler = new ConsoleHandler(facade);
        handler.run();
    }
}