package DBAccess;

import Database.DBConnection;
import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBCountries {

    public static ObservableList<Country> getAllCountries() {

        // Code from Getting The DBConnection Class Project Ready
        ObservableList<Country> clist = FXCollections.observableArrayList();
        // Using 'try' because we don't want to throw any SQL exceptions in the data access code
        // Instead we want to catch and deal with them inside the DB Handler
        try {
            String sql = "SELECT * FROM countries";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country newCountry = new Country(countryId, countryName);

                clist.add(newCountry);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return clist;
    }

    // Code from Getting The DBConnection Class Project Ready
    public static void checkDateConversion() {
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

    }
}
