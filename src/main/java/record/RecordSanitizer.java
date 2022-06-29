package record;

import java.io.File;
import java.io.IOException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import xmlparsing.DomXMLParser;

public class RecordSanitizer {
    private DomXMLParser domXMLParser = new DomXMLParser();

    public Document sanitizeRecord(File xmlFile){
        try {
            Document doc = domXMLParser.parse(xmlFile);

            return doc;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPriceCategoryName(String newName){

    }

    private void setCharCategoryName(String newName, int domainScope){

    }

    private void removeFromAccumulatorSpecification(String elementToRemove, String condition){

    }
}
