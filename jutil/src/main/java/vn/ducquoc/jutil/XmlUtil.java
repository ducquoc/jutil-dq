package vn.ducquoc.jutil;

import java.io.File;
import java.io.FilenameFilter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Helper class for XML operations.
 * 
 * @author ducquoc
 * @see org.apache.commons.lang.StringEscapeUtils
 * @see com.google.enterprise.connector.db.XmlUtils
 */
public class XmlUtil {

  public static String escapeXml(String xmlString) {
    String result = xmlString;
    result.replaceAll("<", "&lt;");
    result.replaceAll(">", "&gt;");
    result.replaceAll("<", "&amp;");
    result.replaceAll("\"", "&quot;");
    result.replaceAll("'", "&apos;"); // &#039;
    result.replaceAll("\\", "&#092;");

    return result;
  }

  public static String unescapeXml(String xmlString) {
    String result = xmlString;
    result.replaceAll("&lt;", "<");
    result.replaceAll("&gt;", ">");
    result.replaceAll("&amp;", "<");
    result.replaceAll("&quot;", "\"");
    result.replaceAll("&apos;", "'"); // &#039;
    result.replaceAll("&#092;", "\\");

    return result;
  }

  public static Node getChildByName(Node parent, String childName) {
    NodeList children = parent.getChildNodes();
    for (int i = 0; i < children.getLength(); ++i) {
      Node child = (Node) children.item(i);
      if (child.getNodeName().equals(childName)) {
        return child;
      }
    }
    return null;
  }

  public static String domToXmlString(Document domDocument) {
    StringWriter writer = new StringWriter();
    javax.xml.transform.Result result = new javax.xml.transform.stream.StreamResult(writer);
    javax.xml.transform.Source source = new javax.xml.transform.dom.DOMSource(domDocument);

    try {
      javax.xml.transform.TransformerFactory.newInstance().newTransformer().transform(source, result);
      writer.close();
    } catch (Exception ex) {
      throw new UtilException("Unable to transform to XML", ex);
    }
    return writer.toString();
  }

  public static Document xmlStringToDom(String xmlString) {
    StringReader xmlReader = new StringReader(xmlString);
    Document domDocument = null;
    try {
      domDocument = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(new org.xml.sax.InputSource(xmlReader));
    } catch (Exception ex) {
      throw new UtilException("Unable to transform to XML", ex);
    }
    return domDocument;
  }

    public static Set<String> compareXmlFiles(String firstFolderPath, String secondFolderPath, boolean outConsole) {
        Set<String> identicalFiles = new HashSet<String>();

        File firstFolder = new File(firstFolderPath);
        File secondFolder = new File(secondFolderPath);

        Map<String, File> firstFileMap = new HashMap<String, File>();
        Map<String, File> secondFileMap = new HashMap<String, File>();

        traverseFolder(firstFileMap, firstFolder, firstFolder);
        traverseFolder(secondFileMap, secondFolder, secondFolder);

        // temporarily - comparison by MD5 instead of XML parsing
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        try {
            saxParser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        System.out.println("Parser: " + saxParser);

        Set<String> sourceKeys = firstFileMap.keySet();
        Set<String> destKeys = secondFileMap.keySet();
        for (String key1: sourceKeys) {
            File file1 = firstFileMap.get(key1);
            File file2 = secondFileMap.get(key1);
            if (file1 != null && file2 != null) {
                try {
                    String node1 = calculateMd5Checksum(file1.getCanonicalPath());
                    String node2 = calculateMd5Checksum(file2.getCanonicalPath());
                    // System.out.println("Source:" + node1 + " Dest:" + node2);
                    if (node1.equals(node2)) {
                        firstFileMap.remove(key1);
                        secondFileMap.remove(key1);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); // can be ignored
                }
            }
        }

        for (String key1 : sourceKeys) {
            if (destKeys.contains(key1)) {
                identicalFiles.add(key1);
                sourceKeys.remove(key1);
                destKeys.remove(key1);
            }
        }

        if (outConsole == true) {
            
        }

        return identicalFiles;
    }

    public static void traverseFolder(Map<String, File> fileMap, File rootFolder, File folderInCheck) {
        if (folderInCheck.isFile()) {
            int length = (int) rootFolder.getAbsolutePath().length();
            String pathKey = folderInCheck.getAbsolutePath().substring(length);
            fileMap.put(pathKey, folderInCheck);
            return;
        }

        String[] names = folderInCheck.list(new FilenameFilter() {
            // for filtering some files, such as ".xml"
            public boolean accept(File dir, String name) {
                return true;
            }
        });

        if (names == null || names.length == 0) {
            return;
        }

        File[] files = folderInCheck.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File child : files) {
            traverseFolder(fileMap, rootFolder, child);
        }
    }

    // TODO: moved to DigestUtil or FileUtil
    public static byte[] createChecksumBytes(String filename, String checksumType) throws Exception {
        if (checksumType == null) { // MD5 by default
            checksumType = "MD5";
        }
        java.io.InputStream fis =  new java.io.FileInputStream(filename);

        byte[] buffer = new byte[1024];
        java.security.MessageDigest complete = java.security.MessageDigest.getInstance(checksumType);
        int numRead = 0;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String calculateMd5Checksum(String filename) throws Exception {
        byte[] b = createChecksumBytes(filename, "MD5");
        String result = "";
        // convert bytes to HEX strings
        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

}
