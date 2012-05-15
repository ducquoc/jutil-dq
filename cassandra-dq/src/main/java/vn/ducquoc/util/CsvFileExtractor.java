package vn.ducquoc.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple class to extract data from CSV and perform basic transformation.
 * 
 * @author ducquoc
 */
public class CsvFileExtractor {

    public static String DEFAULT_DELIMITER = "\t";
    public static String COMMA_DELIMITER = ",";
    public static String PIPE_DELIMITER = "|";

    public String dataFile;
    public String outputDir;
    public Map<String, String> rawData;

    public String delimiter;

    public CsvFileExtractor(String dataFile, String outputDir) {
        this.dataFile = dataFile;
        this.outputDir = outputDir;
        this.rawData = new LinkedHashMap<String, String>();
        this.delimiter = DEFAULT_DELIMITER;
    }

    public CsvFileExtractor(String dataFile, String outputDir, String delimiter) {
        this.dataFile = dataFile;
        this.outputDir = outputDir;
        this.rawData = new LinkedHashMap<String, String>();
        this.delimiter = delimiter;
    }

    public void readRawData(boolean ignoreFirstLine) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            if (ignoreFirstLine) {
                reader.readLine();
            }

            Long uniqueId = 1L;
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                rawData.put(String.valueOf(uniqueId), nextLine);
                uniqueId++;
            }
        } catch (IOException ioEx) {
            throw new RuntimeException("In Out exception", ioEx);
        }
    }

    public Map<String, String> extractData() {
        readRawData(true);

        Map<String, String> result = new LinkedHashMap<String, String>();
        for (String rawEntry : rawData.keySet()) {
            String value = rawData.get(rawEntry).replaceAll(COMMA_DELIMITER, delimiter);
            result.put(rawEntry, value);
        }
        return result;
    }

    public void writeResult(Map<String, String> resultData) {
        PrintWriter writer = newWriter(outputDir + "/output.out");
        PrintWriter crosswalkWriter = newWriter(outputDir + "/crosswalk.out");

        for (String key: resultData.keySet()) {
            String [] fields = resultData.get(key).split(delimiter);
            crosswalkWriter.println(key + delimiter + fields[0]);

            for (int i = 0; i < fields.length - 1; i++) {
                writer.print(fields[i] + PIPE_DELIMITER);
            }
            writer.println(fields[fields.length - 1]);
        }

        writer.close();
        crosswalkWriter.close();
    }

    private PrintWriter newWriter(String filePath) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filePath);
        } catch (IOException ioEx) {
            throw new RuntimeException("In Out exception", ioEx);
        }
        return writer;
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            CsvFileExtractor extractor = new CsvFileExtractor(args[0], args[1]);
            Map<String, String> resultData = extractor.extractData();
            extractor.writeResult(resultData);
        }
        else if (args.length == 3) {
            CsvFileExtractor extractor = new CsvFileExtractor(args[0], args[1], args[2]);
            Map<String, String> resultData = extractor.extractData();
            extractor.writeResult(resultData);
        }
        else {
            System.out.println("Usage: java CsvFileExtractor <data file> <output directory>");
            System.exit(1);
        }
        System.out.println("Done. Output in " + args[1] + " folder.");
    }

}
