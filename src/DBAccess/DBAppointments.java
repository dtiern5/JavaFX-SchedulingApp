package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

public class DBAppointments {

    public static Appointment getAppointment(int appointmentId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments WHERE Appointment_ID = " + Integer.toString(appointmentId);

        DBQuery.setPreparedStatement(conn, selectStatement);

        Appointment appointmentResult;

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentId1 = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalTime startTime = rs.getTime("Start").toLocalTime();
            LocalTime endTime = rs.getTime("End").toLocalTime();
            LocalDate createDate = rs.getDate("Create_Date").toLocalDate();
            String createdBy = rs.getString("Created_By");
            LocalDate lastUpdate = rs.getDate("Last_Update").toLocalDate();
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");

            appointmentResult = new Appointment(appointmentId1, title, description, location,
                    type, startTime, endTime, createDate, createdBy, lastUpdate, lastUpdatedBy,
                    customerId, userId, contactId);

            return appointmentResult;
        }
        return null;
    }


    public static ObservableList getAllAppointments() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        while (rs.next()) {
            appointmentList.add(new Appointment(rs.getInt("Appointment_ID"),
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getString("Type"),
                    rs.getTime("Start").toLocalTime(),
                    rs.getTime("End").toLocalTime(),
                    rs.getDate("Create_Date").toLocalDate(),
                    rs.getString("Created_By"),
                    rs.getDate("Last_Update").toLocalDate(),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),
                    rs.getInt("Contact_ID")));
        }
        return appointmentList;
    }

    public static ObservableList populateAppointmentsTable() {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments";

        try {
            DBQuery.setPreparedStatement(conn, selectStatement);

            PreparedStatement ps = DBQuery.getPreparedStatement();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                appointmentList.add(new Appointment(rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTime("Start").toLocalTime(),
                        rs.getTime("End").toLocalTime(),
                        rs.getDate("Create_Date").toLocalDate(),
                        rs.getString("Created_By"),
                        rs.getDate("Last_Update").toLocalDate(),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentList;
    }
}
