package hr.altima.utils.sanitization;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import hr.altima.utils.xmlparsing.DomXMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XPathSanitizer {
    private final Document doc;

    public XPathSanitizer(File file) throws IOException, SAXException {
        this.doc = DomXMLParser.parse(file);
    }

    public Document setTextContentToAll(String expression, String textContent) throws XPathExpressionException {
        NodeList nodeList = searchByPath(expression);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                element.setTextContent(textContent);
            }
        }
        return doc;
    }

    public Document removeElements(String expression) throws XPathExpressionException {
        NodeList nodeList = searchByPath(expression);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                element.getParentNode().removeChild(element);
            }
        }
        return doc;
    }

    private NodeList searchByPath(String expression) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (NodeList) xPath.compile(expression).evaluate(this.doc, XPathConstants.NODESET);
    }
}
