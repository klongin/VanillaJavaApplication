package report;

/**
 * Entity class that represents a single Report inside of
 * the database.
 *
 * QUESTION:
 * What is the equivalent of PostgreSQL Interval inside of
 * Java?
 */
public class Report {
    private int id;
    private String message_id;
    private Long execution_time;
    private String root_element;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public Long getExecution_time() {
        return execution_time;
    }

    public void setExecution_time(Long execution_time) {
        this.execution_time = execution_time;
    }

    public String getRoot_element() {
        return root_element;
    }

    public void setRoot_element(String root_element) {
        this.root_element = root_element;
    }
}
