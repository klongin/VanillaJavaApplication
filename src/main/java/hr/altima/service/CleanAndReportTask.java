package hr.altima.service;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import hr.altima.Runner;
import hr.altima.dataaccess.repositories.FileRepository;
import hr.altima.dataaccess.repositories.ReportRepository;
import hr.altima.utils.sanitization.XPathSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CleanAndReportTask extends TimerTask {

    private static final FileRepository IN_REPOSITORY =
        new FileRepository(new File("telekomdocuments\\in").getAbsolutePath());
    private static final FileRepository OUT_REPOSITORY =
        new FileRepository(new File("telekomdocuments\\out").getAbsolutePath());
    private static final ReportRepository REPORT_REPOSITORY =
        new ReportRepository(new File("telekomdocuments\\report").getAbsolutePath());
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);
    private static final String XML_PATH_1 = "/getPricesResponse/price/priceCategory/name";
    private static final String XML_PATH_2 =
        "/getPricesResponse/price/priceCharacteristicCollection/priceCharacteristic/characteristic/characteristicCategoryCollection/characteristicCategory[domainId>100]/name";
    private static final String XML_PATH_3 =
        "/getPricesResponse/price/accumulatorSpecification[catalogName[contains(text(),'ACCUMULATOR_SPECIFICATION')]]/balanceGrouping";

    @Override
    public void run() {
        try {
            HashMap<String, Document> cleanFiles = sanitizeFiles(IN_REPOSITORY.getFilesByExtension(".xml"));
            REPORT_REPOSITORY.reportDocuments(cleanFiles.values());
            OUT_REPOSITORY.writeXmlFiles(cleanFiles);
            IN_REPOSITORY.deleteFiles();
            System.out.println("Processed files: " + cleanFiles.size());
        } catch (Exception e) {
            LOGGER.error("ERROR in CleanAndReportTask: ", e);
        }
    }

    private HashMap<String, Document> sanitizeFiles(List<File> files)
        throws XPathExpressionException, IOException, SAXException {
        HashMap<String, Document> documents = new HashMap<>();
        for (File file : files) {
            documents.put(file.getName(), sanitizeFile(file));
        }
        return documents;
    }

    private Document sanitizeFile(File f) throws IOException, SAXException, XPathExpressionException {
        XPathSanitizer xPathParser = new XPathSanitizer(f);
        xPathParser.setTextContentToAll(XML_PATH_1, "UNKNOWN");
        xPathParser.setTextContentToAll(XML_PATH_2, "TOO_LARGE");
        return xPathParser.removeElements(XML_PATH_3);
    }

}
