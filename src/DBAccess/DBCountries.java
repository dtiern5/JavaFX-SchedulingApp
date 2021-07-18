package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBCountries {

    public static Country getCountry(int countryId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM countries WHERE Country_ID = " + countryId;

        DBQuery.setPreparedStatement(conn, selectStatement);

        Country countryResult;

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int countryId1 = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            countryResult = new Country(countryId1, countryName, createDate, createdBy,
                    lastUpdate, lastUpdatedBy);

            return countryResult;
        }
        return null;
    }


    public static ObservableList getAllACountries() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM countries";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        ObservableList<Country> countryList = FXCollections.observableArrayList();

        while (rs.next()) {
            countryList.add(new Country(rs.getInt("Country_ID"),
                    rs.getString("Country"),
                    rs.getString("Create_Date"),
                    rs.getString("Created_By"),
                    rs.getString("Last_Update"),
                    rs.getString("Last_Updated_By")));
        }
        return countryList;
    }


    // Code from Getting The DBConnection Class Project Ready
    /*public static void checkDateConversion() {
        System.out.println("CREATE DATE TEST");
        String sql = "SELECT Create_Date FROM countries";

        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Timestamp ts = rs.getTimestamp("Create_Date");
                System.out.println("Create_Date: " + ts.toLocalDateTime().toString());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }*/
}
