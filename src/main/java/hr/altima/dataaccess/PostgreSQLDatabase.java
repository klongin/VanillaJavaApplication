package hr.altima.dataaccess;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLDatabase {

    private final String url;
    private final String username;
    private final String password;

    Connection connection;

    public PostgreSQLDatabase() {
        // nije mi se dalo komplicirat...
        List<String> credentials = getUserData();
        this.url = credentials.get(0);
        this.username = credentials.get(1);
        this.password = credentials.get(2);
    }

    public void connect() throws SQLException {
        Connection conn = DriverManager.getConnection(url, username, password);
        if (conn != null) {
            this.connection = conn;
        }
    }

    public boolean disconnect() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
            return true;
        }
        return false;
    }

    public List<String> getUserData() {
        BufferedReader reader;
        List<String> credentials = new ArrayList<>();
        try {
            String userCredentials = "C:\\Users\\Karlo\\IdeaProjects\\VanillaJavaApplication\\connection.txt";
            reader = new BufferedReader(new FileReader(userCredentials));
            String line = reader.readLine();
            credentials.add(line);
            while (line != null) {
                line = reader.readLine();
                credentials.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return credentials;
    }

    public List<ArrayList<String>> getData(String query) {
        List<ArrayList<String>> data = new ArrayList<>();

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsMetaData = rs.getMetaData();

            while (rs.next()) {
                ArrayList<String> rows = new ArrayList<>();
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                    rows.add(rs.getString(i));
                }
                data.add(rows);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return data;
    }

    public boolean setData(String query) {
        int rowsUpdated = 0;
        try (Statement st = connection.createStatement()) {
            rowsUpdated = st.executeUpdate(query);
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return rowsUpdated > 0;
    }
}
