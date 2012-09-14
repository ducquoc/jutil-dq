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

            String zipFullFilename = destinationDir + File.separator + zipFilename;

            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFullFilename));
            for (String filename : sourcesFilenames) {
                File fileToZip = new File(filename);
                if (fileToZip.isDirectory()) {
                    addZipEntryDir(zipOut, filename);
                }
                if (fileToZip.isFile()) {
                    addZipEntryFile(zipOut, filename);
                }
            }
            zipOut.close();

            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private static void addZipEntryDir(ZipOutputStream zipOut, String dirPath) throws IOException {
        File zipDir = new File(dirPath);
        String[] dirList = zipDir.list();
        for (int i = 0; i < dirList.length; i++) {
            File fileToZip = new File(zipDir, dirList[i]);
            if (fileToZip.isDirectory()) {
                addZipEntryDir(zipOut, fileToZip.getPath()); // recursively
                continue;
            }
            addZipEntryFile(zipOut, fileToZip.getPath());
        }
        if (dirList.length == 0) { // empty directory
            zipOut.putNextEntry(new ZipEntry(dirPath + File.separator + "."));
            zipOut.closeEntry();
        }
    }

    private static void addZipEntryFile(ZipOutputStream zipOut, String filepath) throws IOException {
        zipOut.putNextEntry(new ZipEntry(filepath));
        byte[] buffer = new byte[1024];
        FileInputStream fin = new FileInputStream(new File(filepath));
        int length;
        while ((length = fin.read(buffer)) > 0) {
            zipOut.write(buffer, 0, length);
        }
        fin.close();
        zipOut.closeEntry();
    }

}
