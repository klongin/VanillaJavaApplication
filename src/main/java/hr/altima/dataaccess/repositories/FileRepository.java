package hr.altima.dataaccess.repositories;

import javax.xml.transform.TransformerException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import hr.altima.utils.xmlparsing.DomXMLParser;
import org.w3c.dom.Document;

public class FileRepository extends Repository {

    public FileRepository(String directory) {
        super(directory);
    }


    public void writeXmlFiles(HashMap<String, Document> documents) throws TransformerException, IOException {
        for (String name : documents.keySet()) {
            writeXmlFile(name, DomXMLParser.stringify(documents.get(name)));
        }
    }

    private void writeXmlFile(String fileName, String content) throws IOException {
        FileWriter writer = new FileWriter(getDirectory() + "\\" + fileName);
        writer.write(content);
        writer.close();
    }

}
