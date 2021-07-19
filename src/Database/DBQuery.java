package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for querying the database.
 */
public class DBQuery {

    private static PreparedStatement statement;

    /**
     * Creates a statement object for use in querying the database.
     * @param conn Connection to the database
     * @param sqlStatement a string that should be in the format of a valid SQL command
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        statement = conn.prepareStatement(sqlStatement);
    }

    /**
     * Returns the PreparedStatement that has been set.
     * @return the PreparedStatement that has been set
     */
    public static PreparedStatement getPreparedStatement() {
        return statement;
    }

}
