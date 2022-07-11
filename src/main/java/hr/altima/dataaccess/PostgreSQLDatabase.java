package hr.altima.dataaccess;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import hr.altima.utils.txtparsing.TextFileParser;

public class PostgreSQLDatabase {

    private final String url;
    private final String username;
    private final String password;
    private Connection connection;

    public PostgreSQLDatabase() {
        try {
            String connectionInfo = TextFileParser.parse(new File("connection.txt").getAbsolutePath());
            String[] credentials = connectionInfo.split("\n");
            this.url = credentials[0];
            this.username = credentials[1];
            this.password = credentials[2];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() throws SQLException {
        Connection conn = DriverManager.getConnection(url, username, password);
        if (conn != null) {
            this.connection = conn;
        }
    }

    public void disconnect() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    public List<ArrayList<String>> getData(String query) throws SQLException {
        List<ArrayList<String>> data = new ArrayList<>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        ResultSetMetaData rsMetaData = rs.getMetaData();

        while (rs.next()) {
            ArrayList<String> rows = new ArrayList<>();
            for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                rows.add(rs.getString(i));
            }
            data.add(rows);
        }
        st.close();
        return data;
    }

    public boolean setData(String query) throws SQLException {
        int rowsUpdated;
        Statement st = connection.createStatement();
        rowsUpdated = st.executeUpdate(query);
        st.close();
        return rowsUpdated > 0;
    }
}
