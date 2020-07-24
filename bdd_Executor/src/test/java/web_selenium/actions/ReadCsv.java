package web_selenium.actions;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import web_selenium.annotations.TestActionArgument;
import web_selenium.annotations.TestActionClass;
import web_selenium.annotations.TestActionOutput;
import web_selenium.annotations.Type;
import web_selenium.base.TestAction;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@TestActionClass(
        description = "Parses CSV data from a file or a string and returns the "
        + "full set of records as an object array.")
@TestActionArgument(name = "file", type = Type.STRING, optional = true,
        description = "The full path to the CSV file to parse.")
@TestActionArgument(name = "csvString", type = Type.STRING, optional = true,
        description = "The CSV string to parse.")
@TestActionArgument(name = "delimiter", type = Type.STRING, optional = true,
        description = "The CSV delimiter character.")
@TestActionArgument(name = "escapeChar", type = Type.STRING, optional = true,
        description = "The CSV escape character.")
@TestActionArgument(name = "recordSeparator", type = Type.STRING, optional = true,
        description = "The CSV record separator character(s).")
@TestActionArgument(name = "excludeBom", type = Type.BOOLEAN, optional = true, defaultValue = "true",
        description = "Specifies whether the file's Byte Order Mark (BOM) should be excluded from "
                + "the CSV content. It is normally safe to leave this argument to the default value "
                + "of \"true\", whether your CSV data contains a BOM or not.")
@TestActionArgument(name = "hasHeader", type = Type.BOOLEAN, optional = true, defaultValue = "false",
        description = "Specifies whether the CSV data starts with a header row.")
@TestActionArgument(name = "format", type = Type.STRING, optional = true,
        description = "The CSV format to use. Valid values are: excel, informix_unload, "
        + "informix_unload_csv, mysql, postgresql_csv, postgresql_text, rfc4180, tdf.")
@TestActionArgument(name = "fieldNames", type = Type.ARRAY, optional = true,
        description = "The CSV field names as an array of strings. If this argument "
        + "is not provided and the CSV has no header row, the column names will become \"col1\", \"col2\", etc.")
@TestActionOutput(name = "records", type = Type.ARRAY,
        description = "The CSV records as an array of objects. The object properties "
        + "will match the field names in the CSV header row. If the header row "
        + "is not available and the \"fieldNames\" argument was not provided, "
        + "the column names will become \"col1\", \"col2\", etc.")
@TestActionOutput(name = "header", type = Type.MAP,
        description = "An object whose properties will match the field names in "
        + "the CSV header row. The values for each property are numbers that "
        + "represent the 0-based index of each field.")
/**
 * An action that parses CSV data from a file or a string and returns its lines
 * as an array of objects.
 */
public class ReadCsv extends TestAction {

    @Override
    public void run() {
        super.run();

        String filePath = this.readStringArgument("file", null);
        String csvString = this.readStringArgument("csvString", null);
        String delimiter = this.readStringArgument("delimiter", null);
        String escapeChar = this.readStringArgument("escapeChar", null);
        String recordSeparator = this.readStringArgument("recordSeparator", null);
        Boolean excludeBom = this.readBooleanArgument("excludeBom", Boolean.TRUE);
        Boolean hasHeader = this.readBooleanArgument("hasHeader", Boolean.FALSE);
        String format = this.readStringArgument("format", "default");
        List<String> fieldNames = this.readArrayArgument("fieldNames", String.class, null);

        try {

            Reader csvReader;
            if (filePath != null) {
                if (excludeBom) {
                    csvReader = new InputStreamReader(new BOMInputStream(new FileInputStream(filePath)), CharEncoding.UTF_8);
                } else {
                    csvReader = Files.newBufferedReader(Paths.get(filePath), Charsets.UTF_8);
                }
            } else if (csvString != null) {
                csvReader = new StringReader(csvString);
            } else {
                throw new RuntimeException("Neither the \"file\" argument, nor the \"csv\" argument were provided.");
            }

            CSVFormat csvFormat = this.getCsvFormat(format);

            if (hasHeader) {
                csvFormat = csvFormat.withFirstRecordAsHeader();
            }

            if (delimiter != null) {
                csvFormat = csvFormat.withDelimiter(delimiter.charAt(0));
            }

            if (escapeChar != null) {
                csvFormat = csvFormat.withEscape(escapeChar.charAt(0));
            }

            if (recordSeparator != null) {
                csvFormat = csvFormat.withRecordSeparator(recordSeparator);
            }

            List<Map<String, String>> recordsArray = new ArrayList<>();
            CSVParser parser = csvFormat.parse(csvReader);
            Iterable<CSVRecord> records = (Iterable<CSVRecord>) parser;

            for (CSVRecord record : records) {
                if (hasHeader) {
                    recordsArray.add(record.toMap());
                } else {
                    Map<String, String> recordsMap = new HashMap<>();
                    Iterator<String> fields = record.iterator();
                    int columnNo = 1;
                    while (fields.hasNext()) {
                        String field = fields.next();
                        if (fieldNames != null && fieldNames.size() >= columnNo && fieldNames.get(columnNo - 1) != null) {
                            recordsMap.put(fieldNames.get(columnNo - 1).trim(), field);
                        } else {
                            recordsMap.put(String.format("col%s", columnNo), field);
                        }

                        columnNo++;
                    }
                    recordsArray.add(recordsMap);
                }
            }

            this.writeOutput("header", parser.getHeaderMap());
            this.writeOutput("records", recordsArray);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse CSV", ex);
        }
    }

    /**
     * Returns a CSVFormat object given the CSV format as a string.
     *
     * @param format
     * @return
     */
    private CSVFormat getCsvFormat(String format) {
        CSVFormat csvFormat = null;

        switch (format.trim().toLowerCase()) {
            case "default":
                csvFormat = CSVFormat.DEFAULT;
                break;
            case "excel":
                csvFormat = CSVFormat.EXCEL;
                break;
            case "informixunload":
            case "informix-unload":
            case "informix_unload":
                csvFormat = CSVFormat.INFORMIX_UNLOAD;
                break;
            case "informixunloadcsv":
            case "informix-unload-csv":
            case "informix_unload_csv":
                csvFormat = CSVFormat.INFORMIX_UNLOAD_CSV;
                break;
            case "mysql":
                csvFormat = CSVFormat.MYSQL;
                break;
            case "postgres":
            case "postgresql-csv":
            case "postgresql_csv":
                csvFormat = CSVFormat.POSTGRESQL_CSV;
                break;
            case "postgresql-text":
            case "postgresql_text":
                csvFormat = CSVFormat.POSTGRESQL_TEXT;
                break;
            case "rfc4180":
                csvFormat = CSVFormat.RFC4180;
            case "tdf":
                csvFormat = CSVFormat.TDF;
            default:
                throw new RuntimeException(String.format("CSV format \"%s\" is not among the supported formats"));
        }

        return csvFormat;
    }
}
