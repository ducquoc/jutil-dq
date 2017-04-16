package vn.ducquoc.jutil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for File/Stream operations.
 * 
 * @author ducquoc
 * @see org.apache.commons.io.IOUtils
 * @see com.google.common.io.CharStreams
 */
public class FileUtil {

  public static java.util.Properties loadProperties(String fileName) {
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    java.util.Properties result = new java.util.Properties();
    try {
      result.load(inputStream);
    }
    catch (IOException ex) {
      throw new UtilException("Unable to load [" + fileName + "]", ex);
    }
    return result;
  }

  public static long[][] loadFileToMatrix(int rows, int cols, String filepath) {
    long[][] array2d = new long[rows][cols];
    String line = "";
    String[] terms = new String[cols];
    try {
      BufferedReader buff = new BufferedReader(new FileReader(new File(filepath)));
      for (int i = 0; i < rows && (line = buff.readLine()) != null; i++) {
        terms = line.split(",");
        for (int j = 0; j < cols; j++) {
          array2d[i][j] = Long.valueOf(terms[j]);
        }
      }
      buff.close();
    } catch (IOException ex) {
      throw new UtilException(ex.getMessage(), ex);
    }
    return array2d;
  }

  public static String readInputStream(InputStream is) throws IOException {
    BufferedInputStream bis = new BufferedInputStream(is);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    int b = -1;
    while ((b = bis.read()) != -1) {
      bos.write(b);
    }
    bis.close();
    is.close();
    bos.flush();
    bos.close();
    return new String(bos.toByteArray());
  }

  public static String readResource(String resourceName) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
    return readInputStream(is);
  }

  public static String readFile(String fileName) throws IOException {
    FileInputStream fis = new FileInputStream(fileName);
    return readInputStream(fis);
  }

  //
  // Java 7+ methods (Eclipse/Maven may require JDK instead of JRE)
  //
  public static String readFileJ7(String path) throws IOException {
    byte[] encoded = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(path));

    return new String(encoded);
  }

  public static String readFileJ7(String path, java.nio.charset.Charset charset) throws IOException {
    byte[] encoded = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(path));

    if (charset == null) {
      charset = java.nio.charset.StandardCharsets.UTF_8;
    }
    return new String(encoded, charset);
  }

}
