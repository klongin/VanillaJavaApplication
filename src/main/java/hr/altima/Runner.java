package hr.altima;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import hr.altima.dataaccess.PostgreSQLDatabase;
import hr.altima.dataaccess.tables.Report;
import hr.altima.utilities.sanitization.XPathSanitizer;
import hr.altima.utilities.xmlparsing.DomXMLParser;
import org.xml.sax.SAXException;

public class Runner {
    public static void main(String[] args) {
        try {
            PostgreSQLDatabase database = new PostgreSQLDatabase();
            File[] files = DomXMLParser.readFiles(
                "C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\telekomdocuments\\in\\");

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                XPathSanitizer xPathParser = null;
                int i = 0;

                @Override
                public void run() {
                    if (i < files.length) {
                        try {
                            xPathParser = new XPathSanitizer(files[i]);

                            System.out.println("Processing: " + files[i].getName());

                            xPathParser.setTextContentToAll(xPathParser.searchByPath(
                                    "/*[local-name()='getPricesResponse']/*[local-name()='price']/*[local-name()='priceCategory']/*[local-name()='name']"),
                                "UNKNOWN");

                            xPathParser.setTextContentToAll(xPathParser.searchByPath(
                                    "/getPricesResponse/price/priceCharacteristicCollection/priceCharacteristic/characteristic/characteristicCategoryCollection/characteristicCategory[domainId>100]/name"),
                                "TOO_LARGE");

                            xPathParser.removeElements(xPathParser.searchByPath(
                                "/getPricesResponse/price/accumulatorSpecification[catalogName[contains(text(),'ACCUMULATOR_SPECIFICATION')]]/balanceGrouping"));


                            DomXMLParser.writeFile(xPathParser.getDocument(), files[i].getName(),
                                "C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\telekomdocuments\\out\\");

                            System.out.println("Processed!");
                            // send the report to postgres

                            Report report =
                                new Report(
                                    xPathParser.getDocument().getElementsByTagName("cb:id").item(0).getTextContent(),
                                    System.currentTimeMillis(),
                                    xPathParser.getDocument().getDocumentElement().getTagName());
                            report.setDatabase(database);
                            report.post();


                            i++;
                        } catch (IOException | SAXException | XPathExpressionException | TransformerException |
                                 SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        timer.cancel();
                    }
                }
            }, 0, 30000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
