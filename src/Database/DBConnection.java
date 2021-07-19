package Database;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class for starting and retrieving database connection
 */
public class DBConnection {

    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ08AkY";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;

    // Driver reference and connection interface reference
    private static Connection conn = null;

    // User name and password
    private static final String userName = "U08AkY";
    private static final String password = "53689231354";


    /*public static Connection startConnectionDriverManager() {
        try {
            conn = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }*/

    /**
     * Starts a connection with the database
     * @return Connection to the database
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static Connection startConnection() throws SQLException {
        MysqlDataSource source = new MysqlDataSource();
        source.setUser(userName);
        source.setPassword(password);
        source.setUrl(jdbcURL);

        conn = (Connection) source.getConnection();
        System.out.println("Connection successful");

        return conn;
    }

    /**
     * Gets the connection to the database. Used anytime after the startConnection() method is run and before
     * closeConnection() is run. More efficient than starting new connections.
     * @return The Connection
     */
    public static Connection getConnection() {
        return conn;
    }

    /**
     * Closes the Connection
     */
    public static void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            //do nothing
        }
        System.out.println("Connection closed");
    }
}
