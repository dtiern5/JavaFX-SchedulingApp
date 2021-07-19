package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Country;
import Model.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database access class for Reports
 */
public class DBReports {

    /**
     * Returns an ObservableList with a count of Appointment types by month.
     *
     * @param year the year to return Appointment types for
     * @return ObservableList of the Report class providing a count of appointment types per month
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static ObservableList countAppointmentTypes(int year) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT Month(Start), Type, Count(*) " +
                "FROM appointments " +
                "WHERE Year(Start) = ? " +
                "GROUP BY Month(Start), Type;";

        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, String.valueOf(year));

        ResultSet rs = ps.executeQuery();

        ObservableList<Report> reportList = FXCollections.observableArrayList();

        while (rs.next()) {
            reportList.add(new Report(Integer.valueOf(rs.getString("Month(Start)")),
                    rs.getString("Type"),
                    rs.getInt("Count(*)")));
        }
        return reportList;
    }


    /**
     * Returns an ObservableList of the number of Customer count per Division in a given Country.
     *
     * @param country the Country of the Customers and Divisions to be returned.
     * @return ObservableList of of the Report class providing a count of Customers per Division
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static ObservableList countDivisions(Country country) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT d.division, Count(*) " +
                "FROM customers AS c " +
                "LEFT JOIN first_level_divisions AS d " +
                "ON d.Division_ID = c.Division_ID " +
                "LEFT JOIN countries AS e " +
                "ON e.Country_ID = d.COUNTRY_ID " +
                "WHERE e.Country = ? " +
                "GROUP BY c.Division_ID";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, String.valueOf(country));

        ResultSet rs = ps.executeQuery();

        ObservableList<Report> reportList = FXCollections.observableArrayList();

        while (rs.next()) {
            reportList.add(new Report(rs.getString("division"),
                    rs.getInt("Count(*)")));
        }
        return reportList;
    }
}
