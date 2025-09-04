package fit.cvut.cz.console.pages;

import java.util.List;
import fit.cvut.cz.console.TextPage;

/**
 * Console help page with auto-aligned columns (syntax : description).
 */
public final class GuidePage implements TextPage {

    /** Single command row. */
    private record Cmd(String syntax, String description) {}

    private static final List<Cmd> CMDS = List.of(
            new Cmd("--file <path>",     "Set CSV file"),
            new Cmd("--stats <codes>",   "Select stats: PP,DM,NH,NU"),
            new Cmd("--export <codes>",  "Select exporters: JSON,XML"),
            new Cmd("--out <dir>",       "Set output directory"),
            new Cmd("--show",            "Compute and print results"),
            new Cmd("--run",             "Compute and export files"),
            new Cmd("--list",            "List available stats/exporters"),
            new Cmd("--help",            "Show this help"),
            new Cmd("--exit",            "Exit")
    );

    @Override
    public String load() {
        int w = CMDS.stream().mapToInt(c -> c.syntax().length()).max().orElse(0) + 2;
        StringBuilder sb = new StringBuilder("Usage:\n");
        for (Cmd c : CMDS) {
            sb.append("  ").append(pad(c.syntax(), w)).append(": ").append(c.description()).append('\n');
        }
        return sb.toString();
    }

    /** Right-pad to a fixed width for neat columns. */
    private static String pad(String s, int w){ int p = w - s.length(); return p<=0? s : s + " ".repeat(p); }
}