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
    private static final String ipAddress = "//us-cdbr-east-04.cleardb.com/";
    private static final String dbName = "heroku_e07515b59a68919";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;

    // Driver reference and connection interface reference
    private static Connection conn = null;

    // User name and password
    private static final String userName = "bd06261092d6da";
    private static final String password = "786c1ce7";


 /*   public static Connection startConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://us-cdbr-east-04.cleardb.com:3306/heroku_e07515b59a68919",
                    userName, password);
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
