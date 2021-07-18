package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDivisions {

    public static Division getDivision(int divisionId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM first_level_divisions WHERE Division_Id = " + Integer.toString(divisionId);

        DBQuery.setPreparedStatement(conn, selectStatement);

        Division divisionResult;

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int divisionId1 = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int countryId = rs.getInt("COUNTRY_ID");

            divisionResult = new Division(divisionId1, division,
                    createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);

            return divisionResult;
        }
        return null;
    }


    public static ObservableList getAllDivisions() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM first_level_divisions";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        ObservableList<Division> divisionList = FXCollections.observableArrayList();

        while (rs.next()) {
            divisionList.add(new Division(rs.getInt("Division_ID"),
                    rs.getString("Division"),
                    rs.getString("Create_Date"),
                    rs.getString("Created_By"),
                    rs.getString("Last_Update"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("COUNTRY_ID")));
        }
        return divisionList;
    }

    public static ObservableList getDivisionByCountryId(int countryId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = '" + countryId + "'";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        ObservableList<Division> divisionList = FXCollections.observableArrayList();

        while (rs.next()) {
            divisionList.add(new Division(rs.getInt("Division_ID"),
                    rs.getString("Division"),
                    rs.getString("Create_Date"),
                    rs.getString("Created_By"),
                    rs.getString("Last_Update"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("COUNTRY_ID")));
        }
        return divisionList;
    }

}



