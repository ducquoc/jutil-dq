package vn.ducquoc.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for extracting data from flat files, usually CSV.
 * 
 * @author ducquoc
 */
public class FlatFileExtractor {

    String dataFile;
    String outputDir;
    Map<String, String> rawData;

    public FlatFileExtractor(String dataFile, String outputDir) {
        this.dataFile = dataFile;
        this.outputDir = outputDir;
        this.rawData = new LinkedHashMap<String, String>();
    }

    public void readRawData(boolean ignoreFirstLine) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            if (ignoreFirstLine) {
                reader.readLine(); // ignore first line
            }

            Long uniqueId = 1L;
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                rawData.put(String.valueOf(uniqueId), nextLine);
                uniqueId++;
            }
        } catch (FileNotFoundException fnfEx) {
            throw new RuntimeException("File Not Found", fnfEx);
        } catch (IOException ioEx) {
            throw new RuntimeException("In Out exception", ioEx);
        }
    }

    public Map<String, String> extractData() {
        readRawData(true);

        Map<String, String> result = new LinkedHashMap<String, String>();
        for (String rawEntry : rawData.keySet()) {
            result.put(rawEntry, rawData.get(rawEntry));
        }
        return result;
    }

    public void writeResult(Map<String, String> resultData) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(outputDir + "/output.out");
        } catch (IOException ioEx) {
            throw new RuntimeException("In Out exception", ioEx);
        }

        for (String key: resultData.keySet()) {
            writer.println(resultData.get(key));
        }
        writer.close();
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            FlatFileExtractor extractor = new FlatFileExtractor(args[0], args[1]);
            Map<String, String> resultData = extractor.extractData();
            extractor.writeResult(resultData);
            System.out.println("Done. Output in " + args[1] + " folder.");
        }
        else {
            System.out.println("Usage: java FlatFileExtractor <data file> <output directory>");
            System.exit(1);
        }
    }

}
