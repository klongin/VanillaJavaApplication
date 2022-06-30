package xmlparsing;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DomXMLParser {
    private static DocumentBuilder builder = createDocumentBuilder();

    private static DocumentBuilder createDocumentBuilder(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document parse(File xmlFile) throws IOException, SAXException {
        return builder.parse(xmlFile);
    }

    // write doc to output stream
    public static void writeXml(Document doc,
                                 OutputStream output)
        throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }


}
