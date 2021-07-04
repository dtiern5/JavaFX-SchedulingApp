package DBAccess;

import Database.DBConnection;
import Database.DBQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUsers {

    public static String getPassword(String userName) throws Exception {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT Password FROM users WHERE User_Name = ?";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, userName);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        String passCheck = null;
        while(rs.next()) {
            passCheck = rs.getString("Password");
        }
        return passCheck;
    }

}
