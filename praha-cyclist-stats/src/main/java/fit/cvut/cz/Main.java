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

public class Main {
    public static void main(String[] args) throws Exception {
        MeasurementReader reader = new CsvMeasurementReader();
        var stats = List.of(
                new AvgPerDayStatistic(), new DayMaxStatistic(),
                new PopularHourStatistic(), new PopularSegmentStatistic()
        );
        var exporters = List.of(new JsonExporter(), new XmlExporter());

        ReportFacade facade = new ReportFacadeImpl(reader, stats, exporters);
        Handler handler = new ConsoleHandler(facade);
        handler.run();
    }
}