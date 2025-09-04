package fit.cvut.cz.console.handler;

import fit.cvut.cz.exporter.Report;
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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public final class ConsoleHandler {

    private final ReportFacade facade;

    public ConsoleHandler() {
        // Собираем фасад с дефолтными компонентами один раз
        MeasurementReader reader = new CsvMeasurementReader();
        var stats = List.of(
                new AvgPerDayStatistic(),       // "PP"
                new DayMaxStatistic(),          // "DM"
                new PopularHourStatistic(),     // "NH"
                new PopularSegmentStatistic()   // "NU"
        );
        var exporters = List.of(new JsonExporter(), new XmlExporter());

        this.facade = new ReportFacadeImpl(reader, stats, exporters);
    }

    public void run() throws Exception {
        printWelcome();
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("(cyclists) > ");
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                if (line.equalsIgnoreCase("--exit")) break;

                String[] t = line.split("\\s+");
                String cmd = t[0].toLowerCase(Locale.ROOT);
                switch (cmd) {
                    case "--help" -> printHelp();
                    case "--file" -> {
                        ensureArgs(t, 2);
                        facade.setCsv(Path.of(t[1]));
                        System.out.println("CSV set: " + t[1]);
                    }
                    case "--stats" -> {
                        ensureArgs(t, 2);
                        facade.setStatCodes(splitCsv(t[1]));
                        System.out.println("Stats set: " + t[1]);
                    }
                    case "--export" -> {
                        ensureArgs(t, 2);
                        facade.setExportCodes(splitCsv(t[1]));
                        System.out.println("Export set: " + t[1]);
                    }
                    case "--out" -> {
                        ensureArgs(t, 2);
                        facade.setOutDir(Path.of(t[1]));
                        System.out.println("Out dir: " + t[1]);
                    }
                    case "--show" -> {
                        Report r = facade.compute();
                        r.statistics().forEach(res ->
                                System.out.println(res.name() + " = " + res.result()));
                    }
                    case "--run" -> {
                        Report r = facade.compute();
                        facade.export(r);
                        System.out.println("Done. Exported files in out dir.");
                    }
                    case "--list" -> {
                        System.out.println("Stats: " + facade.availableStatistics());
                        System.out.println("Exporters: " + facade.availableExporters());
                    }
                    default -> System.out.println("Unknown command. Use --help");
                }
            }
        }
    }

    private static List<String> splitCsv(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).toList();
    }
    private static void ensureArgs(String[] t, int need) {
        if (t.length < need) throw new IllegalArgumentException("Not enough args for " + t[0]);
    }

    private static void printWelcome() {
        System.out.println("""
                Cyclists CLI
                Type --help for commands. Example:
                  --file bc-2020-q1.csv
                  --stats PP,DM,NH,NU
                  --export JSON,XML
                  --out .
                  --run
                """);
    }
    private static void printHelp() {
        System.out.println("""
                Commands:
                  --file <path>         set CSV file
                  --stats <codes>       PP,DM,NH,NU
                  --export <codes>      JSON,XML
                  --out <dir>           output directory (default ".")
                  --show                compute and print to console
                  --run                 compute and export files
                  --list                list available stats/exporters
                  --exit
                """);
    }
}