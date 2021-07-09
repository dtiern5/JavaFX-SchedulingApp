package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime = rs.getTimestamp("End").toLocalDateTime();
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
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
                    rs.getTimestamp("Start").toLocalDateTime(),
                    rs.getTimestamp("End").toLocalDateTime(),
                    rs.getTimestamp("Create_Date").toLocalDateTime(),
                    rs.getString("Created_By"),
                    rs.getTimestamp("Last_Update").toLocalDateTime(),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),
                    rs.getInt("Contact_ID")));
        }
        return appointmentList;
    }

    public static void addAppointment(String title, String description, String location, String type, LocalDateTime start,
                                      LocalDateTime end, String currentUser, int customerId, int userId, int contactId) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String insertStatement = "INSERT INTO appointments(Title, Description, Location, Type, Start, End," +
                "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                "VALUES(?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ?)";

        DBQuery.setPreparedStatement(conn, insertStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setString(7, currentUser);
        ps.setInt(8, customerId);
        ps.setInt(9, userId);
        ps.setInt(10, contactId);

        ps.execute();
    }

    public static void modifyAppointment(String title, String description, String location, String type, LocalDateTime start,
                                         LocalDateTime end, String currentUser, int customerId, int userId, int contactId,
                                         int appointmentId) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String updateStatement = "UPADATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?" +
                "End = ?, Last_Update = NOW(), Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?" +
                "WHERE Appointment_ID = ?";

        DBQuery.setPreparedStatement(conn, updateStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setString(7, currentUser);
        ps.setInt(8, customerId);
        ps.setInt(9, userId);
        ps.setInt(10, contactId);
        ps.setInt(11, appointmentId);

        ps.execute();
    }
}
