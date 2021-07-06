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

public class DBAppointments {

    public static Appointment getAppointment(int appointmentId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sqlStatement = "SELECT * FROM appointments WHERE Appointment_ID = '" + appointmentId + "'";

        DBQuery.setPreparedStatement(conn, sqlStatement);

        Appointment appointmentResult = null;

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

        }



        return appointmentResult;
    }











    public static ObservableList populateAppointmentsTable() {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT appointments.Appointment_ID, appointments.Title, " +
                "appointments.Description, appointments.Location, contacts.Contact_Name, " +
                "appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, " +
                "appointments.User_ID, appointments.Contact_ID FROM appointments " +
                "JOIN contacts ON contacts.Contact_ID = appointments.Contact_ID";

        try {
            DBQuery.setPreparedStatement(conn, selectStatement);

            PreparedStatement ps = DBQuery.getPreparedStatement();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                appointmentList.add(new Appointment(rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Contact_Name"),
                        rs.getString("Type"),
                        rs.getString("Start"),
                        rs.getString("End"),
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
