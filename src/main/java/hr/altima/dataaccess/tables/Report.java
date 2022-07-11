package hr.altima.dataaccess.tables;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity class that represents a single Report inside of
 * the database.
 */
public class Report {
    @JsonProperty("id")
    private int id;
    @JsonProperty("message_id")
    private String messageID;
    @JsonProperty("execution_time")
    private Timestamp executionTime;
    @JsonProperty("root-element")
    private String rootElement;

    public Report() {
        this.messageID = "";
        this.executionTime = new Timestamp(0);
        this.rootElement = "";
    }

    public Report(String messageID, long executionTime, String rootElement) {
        this.messageID = messageID;
        this.executionTime = new Timestamp(executionTime);
        this.rootElement = rootElement;
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

    public void setId(int id) {
        this.id = id;
    }
}
