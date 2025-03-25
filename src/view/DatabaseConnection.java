package view;

import java.sql.*;
import java.util.*;

public class DatabaseConnection {  
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=BanDoAn;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "16124";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
        }
        return conn;
    }

    // Phương thức thực thi câu lệnh SELECT
    public static List<Map<String, Object>> executeQuery(String query, Object... parameters) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi executeQuery: " + e.getMessage());
        }

        return resultList;
    }

    // Phương thức thực thi câu lệnh INSERT, UPDATE, DELETE
    public static int executeUpdate(String query, Object... parameters) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }

            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi executeUpdate: " + e.getMessage());
            return -1;
        }
    }
}
