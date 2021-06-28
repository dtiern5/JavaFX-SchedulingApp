package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    //JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ08AkY";

    //JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;

    //Driver reference and connection interface reference
    private static Connection conn = null;

    //User name and password
    private static final String userName = "U08AkY";
    private static final String password = "53689231354";

    public static Connection startConnection() {
        try {
            conn = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            //do nothing
        }
        System.out.println("Connection closed");
    }
}
