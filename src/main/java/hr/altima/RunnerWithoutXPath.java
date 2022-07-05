package hr.altima;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import hr.altima.dataaccess.PostgreSQLDatabase;
import hr.altima.dataaccess.tables.Report;
import hr.altima.utilities.sanitization.RecordSanitizer;
import hr.altima.utilities.xmlparsing.DomXMLParser;
import org.w3c.dom.Document;

public class RunnerWithoutXPath {


    public static void main(String[] args) {
        try {
            PostgreSQLDatabase database = new PostgreSQLDatabase();
            RecordSanitizer recordSanitizer = new RecordSanitizer();
            File[] files = DomXMLParser.readFiles(
                "C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\telekomdocuments\\in\\");

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                int iterator = 0;

                @Override
                public void run() {
                    if (iterator < files.length) {
                        try {
                            System.out.println("Processing: " + files[iterator].getName());
                            Document doc = recordSanitizer.sanitizeRecord(files[iterator]);

                            DomXMLParser.writeFile(doc, files[iterator].getName(),
                                "C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\telekomdocuments\\out\\");

                            System.out.println("Processed!");
                            // send the report to postgres

                            Report report =
                                new Report(doc.getElementsByTagName("cb:id").item(0).getTextContent(),
                                    System.currentTimeMillis(),
                                    doc.getDocumentElement().getTagName());
                            report.setDatabase(database);
                            report.post();

                            iterator++;
                        } catch (SQLException | IOException | TransformerException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        timer.cancel();
                    }
                }
            }, 0, 1000);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

