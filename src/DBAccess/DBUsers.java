package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Database access class for Users
 */
public class DBUsers {

    /**
     * Returns a boolean 'true' if the given userName parameter is found in the database.
     *
     * @param userName the User Name to search for
     * @return a boolean 'true' if the User Name is found in the database, 'false' if not found
     * @throws SQLException signals SQL Exception has occurred
     */
    public static boolean findUserName(String userName) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT 1 FROM users WHERE User_Name = ?";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, userName);
        ps.execute();

        ResultSet rs = ps.executeQuery();

        return rs.next();
    }

    /**
     * Returns the password for a given User.
     *
     * @param userName the User Name of the password to be returned
     * @return the password of the given User Name
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static String getPassword(String userName) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT Password FROM users WHERE User_Name = ?";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, userName);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        String passCheck = null;
        while (rs.next()) {
            passCheck = rs.getString("Password");
        }
        return passCheck;
    }

    /**
     * Returns a User by the given User ID.
     *
     * @param userId the User ID used for retrieval
     * @return the User being returned
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static User getUser(int userId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM users WHERE User_ID = " + userId;

        DBQuery.setPreparedStatement(conn, selectStatement);

        User userResult;

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int userId1 = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            userResult = new User(userId1, userName, password,
                    createDate, createdBy, lastUpdate, lastUpdatedBy);

            return userResult;
        }
        return null;
    }

    /**
     * Returns a User by the given User Name
     *
     * @param userName the User Name used for retrieval
     * @return the User being returned
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static User getUserByName(String userName) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM users WHERE User_Name = '" + userName + "'";

        DBQuery.setPreparedStatement(conn, selectStatement);

        User userResult;

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String password = rs.getString("Password");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            userResult = new User(userId, userName, password,
                    createDate, createdBy, lastUpdate, lastUpdatedBy);

            return userResult;
        }
        return null;
    }

    /**
     * Returns an ObservableList of all Users in the database.
     *
     * @return ObservableList of Users
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static ObservableList getAllUsers() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM users";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        ObservableList<User> userList = FXCollections.observableArrayList();

        while (rs.next()) {
            userList.add(new User(rs.getInt("User_ID"),
                    rs.getString("User_Name"),
                    rs.getString("Password"),
                    rs.getString("Create_Date"),
                    rs.getString("Created_By"),
                    rs.getString("Last_Update"),
                    rs.getString("Last_Updated_By")));
        }
        return userList;
    }

}
