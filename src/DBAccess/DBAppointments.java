package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;

/**
 * Database access class for Appointments
 */
public class DBAppointments {

    /**
     * Returns an Appointment by the given Appointment ID.
     *
     * @param appointmentId the Appointment ID used for retrieval
     * @return the Appointment being returned
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static Appointment getAppointment(int appointmentId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments WHERE Appointment_ID = " + appointmentId;


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

    /**
     * Returns an ObservableList of all Appointments in the database.
     *
     * @return ObservableList of Appointments
     * @throws SQLException signals a SQL Exception has occurred
     */
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

    /**
     * Returns an ObservableList of Appointments by a String matching a user name.
     *
     * @param user the user string used for retrieval
     * @return Observable List of the Appointments
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static ObservableList getTodaysAppointmentsByUser(String user) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String selectStatement = "SELECT u.User_Name, a.*\n" +
                "FROM users AS u\n" +
                "LEFT JOIN appointments as a\n" +
                "ON u.User_ID = a.User_ID\n" +
                "WHERE a.Start BETWEEN ? AND ?\n" +
                "AND u.User_Name = ?\n" +
                "ORDER BY Start;";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, String.valueOf(LocalDateTime.now(ZoneId.of("UTC"))));
        ps.setString(2, String.valueOf(LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(15)));
        ps.setString(3, user);

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

    /**
     * Returns an ObservableList of all Appointments in the database of the given year and month.
     *
     * @param year the year used for retrieval
     * @param month the month used for retrieval
     * @return Observable List of the appointments
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static ObservableList getAllAppointmentsByMonth(int year, int month) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments WHERE year(start) = ? AND month(start) = ?";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, String.valueOf(year));
        ps.setString(2, String.valueOf(month));

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

    /**
     * Returns an ObservableList of all Appointments in the database of the given week.
     *
     * @param date the date used for retrieval
     * @return Observable List of the appointments
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static ObservableList getAllAppointmentsByWeek(LocalDate date) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments WHERE YEARWEEK(Start, 0) = YEARWEEK(?, 0)";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, String.valueOf(date));

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

    /**
     * Returns a Appointments by the given customer ID.
     *
     * @param customerId the Customer ID used for retrieval
     * @return ObservableList of the appointments
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static ObservableList getAppointmentsByCustomerID(Integer customerId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments WHERE Customer_ID = " + customerId;

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

    /*public static ObservableList getAppointmentsByContact(Contact contact) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM appointments WHERE Contact_ID = " + contact.getContactId();

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
    }*/

    /**
     * Creates an appointment in the database using the given parameters.
     *
     * @param title title of appointment
     * @param description description of appointment
     * @param location location of appointment
     * @param type type of appointment
     * @param start start of appointment
     * @param end end of appointment
     * @param userString userString of appointment
     * @param customerId customerId of appointment
     * @param userId userId of appointment
     * @param contactId contactId of appointment
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static void addAppointment(String title, String description, String location, String type, LocalDateTime start,
                                      LocalDateTime end, String userString, int customerId, int userId, int contactId) throws SQLException {
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
        ps.setString(7, userString);
        ps.setString(8, userString);
        ps.setInt(9, customerId);
        ps.setInt(10, userId);
        ps.setInt(11, contactId);

        ps.execute();
    }

    /**
     * Modifies an Appointment in the database using the given parameters.
     *
     * @param title modified title
     * @param description modified description
     * @param location modified location
     * @param type modified type
     * @param start modified start
     * @param end modified end
     * @param userString modified userString
     * @param customerId modified customerId
     * @param userId modified userId
     * @param contactId modified contactId
     * @param appointmentId modified Appointment ID
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static void modifyAppointment(String title, String description, String location, String type, LocalDateTime start,
                                         LocalDateTime end, String userString, int customerId, int userId, int contactId,
                                         int appointmentId) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String updateStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, " +
                "End = ?, Last_Update = NOW(), Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                "WHERE Appointment_ID = ?";

        DBQuery.setPreparedStatement(conn, updateStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setString(7, userString);
        ps.setInt(8, customerId);
        ps.setInt(9, userId);
        ps.setInt(10, contactId);
        ps.setInt(11, appointmentId);

        ps.execute();
    }

    /**
     * Deletes an Appointment from the database using a given Appointment ID.
     *
     * @param appointmentId the Appointment ID indicating the Appointment to be deleted
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static void deleteAppointment(Integer appointmentId) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = " + appointmentId.toString();
        DBQuery.setPreparedStatement(conn, deleteStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
    }

}
