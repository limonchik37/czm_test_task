# Praha Cyclist Stats

A small **Java 17+ CLI** that reads Prague cyclist counter data from CSV and computes handy statistics—then exports them to **JSON** and **XML**.

> **Built-in stats**
> - **PP** — *Average cyclist count per day*  
> - **DM** — *Day with maximum cyclist count*  
> - **NH** — *Most popular hour of day* (0–23)  
> - **NU** — *Most frequent segment* (`from->to`)

---

## Table of Contents
- [Features](#features)
- [CSV Format](#csv-format)
- [Requirements](#requirements)
- [Build](#build)
- [Run (CLI)](#run-cli)
- [Usage Examples](#usage-examples)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Extend](#extend)
- [Testing](#testing)
- [Notes](#notes)
- [License](#license)

---

## Features
- Fast CSV reader (OpenCSV) → immutable domain model (`Measurement`, `Segment`).
- 4 statistics out of the box (PP/DM/NH/NU).
- Export to **JSON** and **XML** (no heavy dependencies).
- Clean CLI with human-friendly pages/messages.
- Pluggable design: add new stats/exporters without touching core logic.

---

## CSV Format

Expected header (comma-separated by default):

```
locations_id,directions_id,measured_from,measured_to,value
```

Rules:
- `measured_from` / `measured_to` are **ISO-8601 UTC**, e.g. `2020-01-01T10:00:00Z`.
- Empty or non-numeric `value` ⇒ treated as **0**.
- Rows with invalid timestamps are **skipped**.

> If your file uses `;` instead of `,`, set the separator accordingly in `SimpleCsvMeasurementReader`.

---

## Requirements
- **Java 17+**
- **Maven** or **Gradle**
- Dependency: **OpenCSV 5.9**

**Maven**
```xml
<dependency>
  <groupId>com.opencsv</groupId>
  <artifactId>opencsv</artifactId>
  <version>5.9</version>
</dependency>
```

**Gradle (Groovy)**
```gradle
dependencies {
    implementation 'com.opencsv:opencsv:5.9'
}
```

---

## Build

### Maven (with shaded JAR)
```bash
mvn -q package
# if maven-shade-plugin is configured:
java -jar target/*-shaded.jar
```

### Gradle (with shadow/all JAR)
```bash
./gradlew build
# run the fat jar produced by your shadow/all task:
java -jar build/libs/*-all.jar
```

---

## Run (CLI)

Start the app (from IDE or via JAR). You’ll see:

```
(cyclists) >
```

Available commands:
```
--file <path>        set CSV file to read
--stats <codes>      select stats, e.g. PP,DM,NH,NU
--export <codes>     select exporters, e.g. JSON,XML
--out <dir>          output directory (created automatically)
--show               compute and print results
--run                compute and write export.json / export.xml
--list               show available stats and exporters
--help               show help
--exit               exit
```

---

## Usage Examples

**Quick start**
```
(cyclists) > --file src/main/resources/bc-2020-q1.csv
(cyclists) > --stats PP,DM,NH,NU
(cyclists) > --export JSON,XML
(cyclists) > --out out
(cyclists) > --show
Created: YYYY-MM-DDTHH:MM:SSZ
  Average cyclist count per day = 6531.35
  Day with maximum cyclist count = 2020-03-28
  Most popular hour of day = 15
  Most frequent segment = camea-...->camea-...

(cyclists) > --run
Exported:
  out/export.json
  out/export.xml
```

**Paths with spaces**
```
--file "C:\Users\You\Desktop\praha data\bc-2020-q1.csv"
--out  "C:\Users\You\Desktop\cyclists-out"
```

---

## Project Structure

```
src/main/java/
  fit/cvut/cz/
    Main.java

    measurement/         # domain
      Measurement.java
      Segment.java
      CsvMeasurement.java

    reader/              # CSV reader
      MeasurementReader.java
      SimpleCsvMeasurementReader.java

    stats/               # statistics
      Statistic.java
      AvgPerDayStatistic.java      # PP
      DayMaxStatistic.java         # DM
      PopularHourStatistic.java    # NH
      PopularSegmentStatistic.java # NU

    report/
      Result.java
      Report.java

    export/              # exporters
      Exporter.java
      JsonExporter.java
      XmlExporter.java

    facade/              # orchestration
      ReportFacade.java
      SimpleReportFacade.java

    console/             # CLI
      Handler.java
      CliHandler.java
      pages/
        Page.java
        ConsoleTextPage.java
        StartPage.java
        GuidePage.java
        ErrorResponse.java
        SuccessResponse.java
        FileSetPage.java
        StatsSetPage.java
        ExportSetPage.java
        OutDirSetPage.java
        ComputeResultPage.java
        ExportDonePage.java
        ListAvailablePage.java
```

---

## Architecture

- **Reader**: parses CSV → `List<Measurement>` (immutable records).
- **Statistics** (`Statistic`): pure functions over the dataset, returning a display string.
- **Exporters** (`Exporter`): write a `Report` to a file (JSON/XML).
- **Facade**: holds current configuration (CSV path, selected stats/exporters, output dir) and exposes
  - `compute()` → `Report`
  - `export(report)` → list of written files
- **CLI**: parses commands, delegates to the Facade, prints “pages” (clean separation from logic).

Why this design?
- Clear separation of concerns and easy unit testing.
- Adding a new stat/exporter = add one class + register it.
- CLI remains thin and declarative.

---

## Extend

**Add a statistic**
```java
public final class MyCoolStat implements Statistic {
  public String code() { return "MC"; }
  public String name() { return "My cool stat"; }
  public String compute(List<Measurement> data) { /* ... */ return "42"; }
}
```
Register it when building the Facade (e.g., in `Main`).

**Add an exporter**
```java
public final class CsvExporter implements Exporter {
  public String code() { return "CSV"; }
  public void export(Report report, Path out) throws IOException { /* write file */ }
}
```

**Different CSV layout?**  
Adjust/replace `SimpleCsvMeasurementReader` (separator, header mapping, etc.).

---

## Testing

Unit tests cover the reader, statistics, exporters and a small end-to-end facade test.

Run:
```bash
mvn -q test
# or
./gradlew test
```

---

## Notes
- All timestamps are handled as **UTC** (`Instant`).
- Empty/invalid `value` → `0`. Rows with invalid time are skipped.
- The output directory is **created automatically** before export.
- Avoid committing large datasets—use `.gitignore` or Git LFS.

**Suggested `.gitignore`:**
```
/target/
/build/
/out/
/.idea/
/*.iml
src/main/resources/*.csv
```

---

## License

Choose a license that fits your needs (e.g., **MIT**) and add it here.
