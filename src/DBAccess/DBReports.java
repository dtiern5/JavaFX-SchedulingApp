package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBReports {

    public static ObservableList countAppointmentTypes(int year, int month) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT Type, Count(*) FROM appointments " +
                "WHERE YEAR(Start) = ? AND MONTH(Start) = ? GROUP BY Type";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, String.valueOf(year));
        ps.setString(2, String.valueOf(month));

        ResultSet rs = ps.executeQuery();

        ObservableList<Report> reportList = FXCollections.observableArrayList();

        while (rs.next()) {
            reportList.add(new Report(rs.getString("Type"),
            rs.getInt("Count(*)")));
        }
        return reportList;
    }
}

