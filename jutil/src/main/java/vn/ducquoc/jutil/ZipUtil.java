package vn.ducquoc.jutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Helper class for Zip operations.
 * 
 * @author ducquoc
 * @see org.apache.commons.io.IOUtils
 * @see com.google.common.io.CharStreams
 */
public class ZipUtil {

    public static Boolean doZip(String zipFilename, String destinationDir, List<String> sourcesFilenames) {
        try {
            if (destinationDir == null || destinationDir.trim().equals("")) {
                destinationDir = ".";
            }
            boolean newlyCreatedDir = (new File(destinationDir)).mkdirs();
            if (newlyCreatedDir) {
                System.out.println("Created directory " + destinationDir);
            }

            String zipFullFilename = destinationDir + "/" + zipFilename;

            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFullFilename));
            for (String filename : sourcesFilenames) {
                zipOut.putNextEntry(new ZipEntry(filename));
                File fileToZip = new File(filename);
                if (fileToZip.isFile()) {
                    addZipEntryFile(zipOut, fileToZip);
                }
                zipOut.closeEntry();
            }
            zipOut.close();

            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static void addZipEntryFile(ZipOutputStream zipOut, File fileToZip) throws IOException {
        byte[] buffer = new byte[1024];
        FileInputStream fin = new FileInputStream(fileToZip);
        int length;
        while ((length = fin.read(buffer)) > 0) {
            zipOut.write(buffer, 0, length);
        }
        fin.close();
    }


    public static void main(String[] args) {
        String[] fileNames = { "src/test/java/vn/ducquoc/jutil/AppTest.java",
                "src/test/java/vn/ducquoc/jutil/HealthcareUtilTest.java" };
        List<String> names = Arrays.asList(fileNames);
        ZipUtil.doZip("zip-dq.zip", "target", names);
    }

}
