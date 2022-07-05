package hr.altima.utilities.sanitization;

import java.io.File;
import java.io.IOException;
import hr.altima.utilities.xmlparsing.DomXMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RecordSanitizer {
    private Document doc = null;

    public Document sanitizeRecord(File xmlFile) {
        try {
            this.doc = DomXMLParser.parse(xmlFile);
            setPriceCategoryName("UNKNOWN");
            setCharCategoryName("TOO_LARGE", 100);
            removeWhereElementHasCondition("pc:accumulatorSpecification", "pc:balanceGrouping", "cb:catalogName",
                "ACCUMULATOR_SPECIFICATION");

            return doc;
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPriceCategoryName(String newName) {
        NodeList priceCategories = this.doc.getElementsByTagName("pc:priceCategory");
        for (int i = 0; i < priceCategories.getLength(); i++) {
            Node pctg = priceCategories.item(i);
            if (pctg.getNodeType() == Node.ELEMENT_NODE) {
                Element priceCategory = (Element) pctg;
                NodeList nameList = priceCategory.getChildNodes();
                for (int j = 0; j < nameList.getLength(); j++) {
                    Node n = nameList.item(j);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element name = (Element) n;
                        if (name.getTagName().equals("pc:name")) {
                            name.setTextContent(newName);
                        }
                    }
                }
            }
        }
    }


    public void setCharCategoryName(String newValue, int domainScope) {
        NodeList charCategories = this.doc.getElementsByTagName("pc:characteristicCategory");
        for (int i = 0; i < charCategories.getLength(); i++) {
            Node cctg = charCategories.item(i);
            if (cctg.getNodeType() == Node.ELEMENT_NODE) {
                Element charCategory = (Element) cctg;
                NodeList elementList = charCategory.getChildNodes();
                int value = 0;
                Node name = null;
                for (int j = 0; j < elementList.getLength(); j++) {
                    Node n = elementList.item(j);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element childNode = (Element) n;
                        if (childNode.getTagName().equals("pc:name")) {
                            name = n;
                        }
                        if (childNode.getTagName().equals("pc:domainId")) {
                            value = Integer.parseInt(childNode.getTextContent());
                        }
                        if (value > domainScope) {
                            if (name != null) {
                                name.setTextContent(newValue);
                            }
                            value = 0;
                        }
                    }
                }
            }
        }
    }

    public void removeWhereElementHasCondition(String tagName, String elementToRemove, String elementThatHasCondition,
                                               String condition) {
        NodeList childNodes = this.doc.getElementsByTagName(tagName);
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) childNode;
                NodeList childNodesOfElement = element.getChildNodes();
                boolean toRemove = false;
                boolean isFound = false;
                Node balanceGrouping = null;
                for (int j = 0; j < childNodesOfElement.getLength(); j++) {
                    Node n = childNodesOfElement.item(j);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) n;
                        if (e.getTagName().equals(elementToRemove)) {
                            balanceGrouping = n;
                            isFound = true;
                        }
                        if (e.getTagName().equals(elementThatHasCondition)) {
                            if (e.getTextContent().equals(condition)) {
                                toRemove = true;
                            }
                        }
                        if (toRemove) {
                            if (isFound) {
                                balanceGrouping.getParentNode().removeChild(balanceGrouping);
                                toRemove = false;
                            }
                        }
                    }
                }
            }
        }
    }


}
