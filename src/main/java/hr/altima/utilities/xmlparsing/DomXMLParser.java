package hr.altima.utilities.xmlparsing;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DomXMLParser {
    private static DocumentBuilder builder = createDocumentBuilder();

    private static DocumentBuilder createDocumentBuilder() {
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

    public static File[] readFiles(String directory) throws IOException {
        File dir = new File(directory);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".xml"));
            return files;
        }
        return new File[0];
    }

    public static void writeFile(Document document, String fileName, String directory)
        throws IOException, TransformerException {
        FileOutputStream fos = new FileOutputStream(directory + fileName);
        writeXml(document, fos);
    }

    public static void writeFiles(Document[] documents, String directory) throws IOException, TransformerException {
        FileOutputStream fos = null;
        int num = 0;
        for (Document xmlDoc : documents) {
            fos = new FileOutputStream(directory + "sanitized" + num + ".xml");
            writeXml(xmlDoc, fos);
            num++;
        }
    }


}
