package vn.ducquoc.jutil;

import java.io.StringReader;
import java.io.StringWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

}
