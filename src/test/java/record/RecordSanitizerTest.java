package record;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import xmlparsing.DomXMLParser;

public class RecordSanitizerTest {

    private RecordSanitizer recordSanitizer = new RecordSanitizer();

    public static Document setPriceCategoryName(Document newDoc, String newName){
        NodeList priceCategories = newDoc.getElementsByTagName("pc:priceCategory");
        for(int i = 0; i < priceCategories.getLength(); i++){
            Node pctg = priceCategories.item(i);
            if(pctg.getNodeType() == Node.ELEMENT_NODE){
                Element priceCategory = (Element)pctg;
                NodeList nameList = priceCategory.getChildNodes();
                for (int j = 0; j < nameList.getLength();j++){
                    Node n = nameList.item(j);
                    if(n.getNodeType()==Node.ELEMENT_NODE){
                        Element name = (Element)n;
                        if(name.getTagName().equals("pc:name")){
                            name.setTextContent(newName);
                        }
                    }
                }
            }
        }
        return newDoc;
    }

    public static Document setCharCategoryName(Document newDoc, String newValue, int domainScope){
        NodeList charCategories = newDoc.getElementsByTagName("pc:characteristicCategory");

        for(int i = 0; i < charCategories.getLength(); i++){
            Node cctg = charCategories.item(i);
            if(cctg.getNodeType() == Node.ELEMENT_NODE){
                Element charCategory = (Element)cctg;
                NodeList elementList = charCategory.getChildNodes();
                int value = 0;
                Node name = null;
                for (int j = 0; j < elementList.getLength();j++){
                    Node n = elementList.item(j);
                    if(n.getNodeType()==Node.ELEMENT_NODE){
                        Element childNode = (Element)n;
                        // when you arrive upon a name node, store it
                        if(childNode.getTagName().equals("pc:name")){
                            name = n;
                            System.out.println("set name to external reference so it can be changed in case domain is greater than 100");
                        }
                        // when you arrive upon a domainId node, change value
                        if(childNode.getTagName().equals("pc:domainId")){
                            value = Integer.parseInt(childNode.getTextContent());
                            System.out.println("arrived upon domainId");
                        }
                        // when value is greater than 100, change to TOO_LARGE
                        if(value > domainScope){
                            assert name != null;
                            name.setTextContent(newValue);
                            value = 0; // set to 0 so it does not return
                            System.out.println("greater than 100");
                        }
                    }
                }
            }
        }
        return newDoc;
    }

    public static Document removeWhereElementHasCondition(Document newDoc, String tagName, String elementToRemove, String elementThatHasCondition, String condition){
        NodeList childNodes = newDoc.getElementsByTagName(tagName);
        for(int i = 0; i < childNodes.getLength(); i++){
            Node childNode = childNodes.item(i);
            if(childNode.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element)childNode;
                NodeList childNodesOfElement = element.getChildNodes();
                boolean toRemove = false;
                boolean isFound = false;
                Node balanceGrouping = null;
                for (int j = 0; j < childNodesOfElement.getLength();j++){
                    Node n = childNodesOfElement.item(j);
                    if(n.getNodeType()==Node.ELEMENT_NODE){
                        Element e = (Element)n;
                        if(e.getTagName().equals(elementToRemove)){
                            balanceGrouping = n;
                            isFound = true;
                        }
                        if(e.getTagName().equals(elementThatHasCondition)){
                            if(e.getTextContent().equals(condition)){
                                toRemove = true;
                                System.out.println("arrived upon catalogName which has the condition");
                            }
                        }
                        if(toRemove){
                            if(isFound) {
                                System.out.println("remove element");
                                balanceGrouping.getParentNode().removeChild(balanceGrouping);
                                toRemove = false;
                            }
                        }
                    }
                }
            }
        }
        return newDoc;
    }


    @Test
    void testPriceCategoryName(){
        File xmlFile = new File("C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\src\\test\\java\\record\\in\\example.xml");
        Document doc = null;
        try {
            doc = DomXMLParser.parse(xmlFile);

            Document doc2 = setPriceCategoryName(doc, "UNKNOWN");

            try (FileOutputStream output =
                     new FileOutputStream("C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\src\\test\\java\\record\\out\\sanitized.xml")) {
                DomXMLParser.writeXml(doc2, output);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCharCategoryName(){
        File xmlFile = new File("C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\src\\test\\java\\record\\in\\example.xml");
        Document doc = null;
        try {
            doc = DomXMLParser.parse(xmlFile);

            Document doc2 = setCharCategoryName(doc, "TOO_LARGE",100);

            try (FileOutputStream output =
                     new FileOutputStream("C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\src\\test\\java\\record\\out\\sanitized.xml")) {
                DomXMLParser.writeXml(doc2, output);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testWhereElementHasCondition(){
        File xmlFile = new File("C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\src\\test\\java\\record\\in\\example.xml");
        Document doc = null;
        try {
            doc = DomXMLParser.parse(xmlFile);

            Document doc2 = removeWhereElementHasCondition(doc, "pc:accumulatorSpecification","pc:balanceGrouping","cb:catalogName","ACCUMULATOR_SPECIFICATION");

            try (FileOutputStream output =
                     new FileOutputStream("C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\src\\test\\java\\record\\out\\sanitized.xml")) {
                DomXMLParser.writeXml(doc2, output);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sanitizeRecord(){
        File xmlFile = new File("C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\src\\test\\java\\record\\in\\example.xml");
        Document doc = this.recordSanitizer.sanitizeRecord(xmlFile);

        try (FileOutputStream output =
                 new FileOutputStream("C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\src\\test\\java\\record\\out\\sanitized.xml")) {
            DomXMLParser.writeXml(doc, output);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

    }

}
