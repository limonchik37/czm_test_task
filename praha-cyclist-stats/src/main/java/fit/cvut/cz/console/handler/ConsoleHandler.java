package fit.cvut.cz.console.handler;

import fit.cvut.cz.console.pages.GuidePage;
import fit.cvut.cz.console.pages.StartPage;
import fit.cvut.cz.console.pages.ComputeResultPage;
import fit.cvut.cz.console.pages.response.ErrorResponse;
import fit.cvut.cz.console.pages.ExportDonePage;
import fit.cvut.cz.console.pages.ExportSetPage;
import fit.cvut.cz.console.pages.FileSetPage;
import fit.cvut.cz.console.pages.ListAvailablePage;
import fit.cvut.cz.console.pages.OutDirSetPage;
import fit.cvut.cz.console.pages.StatsSetPage;
import fit.cvut.cz.console.pages.response.SuccessResponse;
import fit.cvut.cz.exporter.Report;
import fit.cvut.cz.facade.ReportFacade;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Simple CLI loop: parses commands, delegates to {@link ReportFacade},
 * and prints responses using console "pages".
 */
public final class ConsoleHandler implements Handler {

    private final ReportFacade facade;
    private final Scanner in;

    /**
     * @param facade orchestrates reader, statistics, and exporters
     */
    public ConsoleHandler(ReportFacade facade) {
        this.facade = Objects.requireNonNull(facade);
        this.in = new Scanner(System.in);
    }

    /**
     * Starts the REPL-style loop.
     * Supported commands: --help, --exit, --file, --stats, --export, --out, --list, --show, --run.
     */
    @Override
    public void run() {
        System.out.print(new StartPage().load());
        System.out.print(new GuidePage().load());

        while (true) {
            System.out.print("(cyclists) > ");
            String line = safeReadLine();
            if (line == null) break;
            if (line.isBlank()) continue;

            // supports quoted paths with spaces
            List<String> args = tokenize(line);
            String cmd = args.get(0).toLowerCase(Locale.ROOT);

            try {
                switch (cmd) {
                    case "--help" -> System.out.print(new GuidePage().load());
                    case "--exit" -> {
                        System.out.print(new SuccessResponse("Bye").load());
                        return;
                    }

                    case "--file" -> {
                        requireArgs(cmd, args, 2);
                        Path p = Path.of(args.get(1));
                        facade.setCsv(p);
                        System.out.print(new FileSetPage(p).load());
                    }

                    case "--stats" -> {
                        requireArgs(cmd, args, 2);
                        var codes = splitCsv(args.get(1));
                        facade.setStatCodes(codes);
                        System.out.print(new StatsSetPage(codes).load());
                    }

                    case "--export" -> {
                        requireArgs(cmd, args, 2);
                        var codes = splitCsv(args.get(1));
                        facade.setExportCodes(codes);
                        System.out.print(new ExportSetPage(codes).load());
                    }

                    case "--out" -> {
                        requireArgs(cmd, args, 2);
                        Path dir = Path.of(args.get(1));
                        facade.setOutDir(dir);
                        System.out.print(new OutDirSetPage(dir).load());
                    }

                    case "--list" -> {
                        var stats = facade.availableStatistics();
                        var exps = facade.availableExporters();
                        System.out.print(new ListAvailablePage(stats, exps).load());
                    }

                    case "--show" -> {
                        Report r = facade.compute();
                        System.out.print(new ComputeResultPage(r).load());
                    }

                    case "--run" -> {
                        Report r = facade.compute();
                        var files = facade.export(r);
                        System.out.print(new ExportDonePage(files).load());
                    }

                    default -> System.out.print(new ErrorResponse("Unknown command: " + cmd).load());
                }
            } catch (Exception e) {
                System.out.print(new ErrorResponse(e.getMessage() == null ? e.toString() : e.getMessage()).load());
            }
        }
    }

    // ---------- helpers ----------

    /**
     * Ensures the command has at least {@code need} arguments.
     * @throws IllegalArgumentException if not enough args
     */
    private static void requireArgs(String cmd, List<String> args, int need) {
        if (args.size() < need) throw new IllegalArgumentException("Not enough args for " + cmd);
    }

    /**
     * Splits comma-separated codes, normalizes to UPPER, trims, and de-duplicates.
     */
    private static List<String> splitCsv(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim).filter(s -> !s.isEmpty())
                .map(s -> s.toUpperCase(Locale.ROOT))
                .distinct().collect(Collectors.toList());
    }

    /**
     * Tokenizes a line while respecting double quotes for paths with spaces.
     * Example: --file "C:\my data\file.csv"
     */
    private static List<String> tokenize(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                continue;
            }
            if (Character.isWhitespace(c) && !inQuotes) {
                if (cur.length() > 0) {
                    out.add(cur.toString());
                    cur.setLength(0);
                }
            } else cur.append(c);
        }
        if (cur.length() > 0) out.add(cur.toString());
        if (out.isEmpty()) out.add(""); // keep at least one token
        return out;
    }

    /**
     * Reads a line from stdin; returns {@code null} on EOF.
     */
    private String safeReadLine() {
        try {
            return in.nextLine();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}