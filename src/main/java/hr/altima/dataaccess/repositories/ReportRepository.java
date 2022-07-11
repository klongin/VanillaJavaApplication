package hr.altima.dataaccess.repositories;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import hr.altima.dataaccess.PostgreSQLDatabase;
import hr.altima.dataaccess.tables.Report;
import hr.altima.utils.jsonparsing.JsonParser;
import org.w3c.dom.Document;

public class ReportRepository extends Repository {

    private final PostgreSQLDatabase db;

    private static final String INSERT_QUERY_FORMAT =
        "INSERT INTO report (message_id,execution_time,root_element) VALUES('%s', '%s', '%s');";

    public ReportRepository(String directory) {
        super(directory);
        db = new PostgreSQLDatabase();
    }

    public void reportDocuments(Collection<Document> documents) throws IOException, SQLException {
        for (Document document : documents) {
            reportDocument(document);
        }
    }

    private void reportDocument(Document d) throws IOException, SQLException {
        Report report =
            new Report(d.getElementsByTagName("cb:id").item(0).getTextContent(),
                System.currentTimeMillis(), d.getDocumentElement().getTagName());
        postReport(report);
        JsonParser.convertObjectToJson(report, getDirectory(), String.valueOf(report.getMessageID()));
    }

    private void postReport(Report report) throws SQLException {
        db.connect();
        String query = String.format(INSERT_QUERY_FORMAT, report.getMessageID(), report.getExecutionTime(),
            report.getRootElement());
        db.setData(query);
        db.disconnect();
    }

}
