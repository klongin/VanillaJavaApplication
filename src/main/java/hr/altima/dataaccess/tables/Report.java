package hr.altima.dataaccess.tables;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import hr.altima.dataaccess.PostgreSQLDatabase;

/**
 * Entity class that represents a single Report inside of
 * the database.
 */
public class Report {
    private int id;
    private String messageID;
    private Timestamp executionTime;
    private String rootElement;

    private PostgreSQLDatabase db;

    public Report(String messageID, Long executionTime, String rootElement) {
        this.messageID = messageID;
        this.executionTime = new Timestamp(executionTime);
        this.rootElement = rootElement;
    }


    public boolean fetch() throws SQLException {
        this.db.connect();
        List<ArrayList<String>> list;
        String query = String.format("SELECT * FROM report WHERE id = %d;", getID());
        list = db.getData(query);
        try {
            setMessageID(list.get(0).get(1));
            setExecutionTime(Timestamp.valueOf(list.get(0).get(2)));
            setRootElement(list.get(0).get(3));
            return this.db.disconnect();
        } catch (IndexOutOfBoundsException ioobe) {
            return false;
        }
    }

    public boolean put() throws SQLException {
        db.connect();
        String query =
            "UPDATE artist SET id = " + getID() + ", message_id = " + getMessageID() + ", execution_time = " +
                getExecutionTime() + ", root_element = " + getRootElement() + ";";
        if (db.setData(query)) {
            return db.disconnect();
        }
        return false;
    }

    public void post() throws SQLException {
        db.connect();
        String query =
            "INSERT INTO report (message_id,execution_time,root_element) VALUES('" + getMessageID() + "', '" +
                getExecutionTime() + "', '" +
                getRootElement() + "');";
        if (db.setData(query)) {
            setId(Integer.parseInt(db.getData("SELECT currval(pg_get_serial_sequence('report','id'));").get(0).get(0)));
            db.disconnect();
        }
    }

    public boolean remove() throws SQLException {
        db.connect();
        String query = String.format("DELETE FROM report WHERE id = %d;", getID());
        if (!db.setData(query)) {
            db.disconnect();
            return false;
        }
        return db.disconnect();
    }


    public int getID() {
        return id;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public Timestamp getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Timestamp executionTime) {
        this.executionTime = executionTime;
    }

    public String getRootElement() {
        return rootElement;
    }

    public void setRootElement(String rootElement) {
        this.rootElement = rootElement;
    }

    public void setDatabase(PostgreSQLDatabase db) {
        this.db = db;
    }

    public void setId(int id) {
        this.id = id;
    }
}
