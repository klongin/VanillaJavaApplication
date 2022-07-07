package hr.altima.service;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TimerTask;
import hr.altima.Runner;
import hr.altima.dataaccess.PostgreSQLDatabase;
import hr.altima.dataaccess.tables.Report;
import hr.altima.utilities.jsonparsing.JsonParser;
import hr.altima.utilities.sanitization.XPathSanitizer;
import hr.altima.utilities.xmlparsing.DomXMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CleanAndReportTask extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);
    private static final PostgreSQLDatabase database = new PostgreSQLDatabase();
    private static final String xmlTask1 = "/getPricesResponse/price/priceCategory/name";
    private static final String xmlTask2 =
        "/getPricesResponse/price/priceCharacteristicCollection/priceCharacteristic/characteristic/characteristicCategoryCollection/characteristicCategory[domainId>100]/name";
    private static final String xmlTask3 =
        "/getPricesResponse/price/accumulatorSpecification[catalogName[contains(text(),'ACCUMULATOR_SPECIFICATION')]]/balanceGrouping";

    private String getAbsolutePath(String localPath) {
        File file = new File(localPath);
        return file.getAbsolutePath();
    }

    private Document cleanFile(File f) throws IOException, SAXException, XPathExpressionException {
        XPathSanitizer xPathParser = new XPathSanitizer(f);
        xPathParser.setTextContentToAll(xPathParser.searchByPath(xmlTask1), "UNKNOWN");
        xPathParser.setTextContentToAll(xPathParser.searchByPath(xmlTask2), "TOO_LARGE");
        xPathParser.removeElements(xPathParser.searchByPath(xmlTask3));
        return xPathParser.getDocument();
    }

    private void reportDocument(Document document) throws SQLException, IOException {
        Report report =
            new Report(document.getElementsByTagName("cb:id").item(0).getTextContent(),
                System.currentTimeMillis(),
                document.getDocumentElement().getTagName());
        report.setDatabase(database);
        report.post();
        JsonParser.objectToJson(report, getAbsolutePath("telekomdocuments\\report"), String.valueOf(report.getID()));
    }

    @Override
    public void run() {
        try {
            File[] dirtyFiles = DomXMLParser.readFiles(getAbsolutePath("telekomdocuments\\in"));
            for (File f : dirtyFiles) {
                System.out.println("Processing: " + f.getName());
                Document cleanDocument = cleanFile(f);
                DomXMLParser.writeDocumentToFile(cleanDocument, getAbsolutePath("telekomdocuments\\out"),
                    f.getName());
                reportDocument(cleanDocument);
                System.out.println("Processed!");
            }
            if (!DomXMLParser.deleteFiles(dirtyFiles)) {
                throw new IOException("Files were not deleted properly");
            }
        } catch (IOException e) {
            logger.error("Error while writing file to out directory: " + e.getMessage());
        } catch (SAXException | TransformerException e) {
            logger.error("Error while parsing the XML document: " + e.getMessage());
        } catch (XPathExpressionException e) {
            logger.error("Error with XPath expression: " + e.getMessage());
        } catch (SQLException e) {
            logger.error("Error while posting Record to database: " + e.getMessage());
        }
    }
}
