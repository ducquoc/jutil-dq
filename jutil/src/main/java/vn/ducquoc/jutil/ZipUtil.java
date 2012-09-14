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
                File fileToZip = new File(filename);
                if (fileToZip.isDirectory()) {
                    addZipEntryDir(zipOut, filename);
                }
                if (fileToZip.isFile()) {
                    zipOut.putNextEntry(new ZipEntry(filename));
                    addZipEntryFile(zipOut, fileToZip);
                    zipOut.closeEntry();
                }
            }
            zipOut.close();

            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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

    private static void addZipEntryDir(ZipOutputStream zipOut, String dirPath) throws IOException {
        File zipDir = new File(dirPath);
        String[] dirList = zipDir.list();
        for (int i = 0; i < dirList.length; i++) {
            File fileToZip = new File(zipDir, dirList[i]);
            if (fileToZip.isDirectory()) {
                String filePath = fileToZip.getPath();
                addZipEntryDir(zipOut, filePath); // recursively
                continue;
            }
            ZipEntry anEntry = new ZipEntry(fileToZip.getPath());
            zipOut.putNextEntry(anEntry);
            FileInputStream fis = new FileInputStream(fileToZip);
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zipOut.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        }
    }


    public static void main(String[] args) {
        String[] fileNames = { "src/test/java/vn/ducquoc/jutil", "src/main/java/vn/ducquoc/jutil/HealthcareUtil.java" };
        List<String> names = Arrays.asList(fileNames);

        boolean zipSuccess = ZipUtil.doZip("zip-dq.zip", ".", names);
        System.out.println(zipSuccess ? "Zip done!" : "Failed to zip...");
    }

}
