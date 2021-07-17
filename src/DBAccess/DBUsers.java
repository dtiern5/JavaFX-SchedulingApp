package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.User;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsers {

    public static boolean findUserName(String userName) throws Exception {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT 1 FROM users WHERE User_Name = ?";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, userName);
        ps.execute();

        ResultSet rs = ps.executeQuery();

        return rs.next();
    }


    public static String getPassword(String userName) throws Exception {
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

    public static User getUser(int userId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM users WHERE User_ID = " + Integer.toString(userId);

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
