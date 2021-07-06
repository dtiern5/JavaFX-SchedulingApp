package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Appointments;
import Model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAppointments {

    public static ObservableList populateAppointmentsTable() {

        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, contacts.Contact_Name, appointments.Type,\n" +
                "appointments.Start, appointments.End, appointments.Customer_ID, appointments.User_ID, appointments.Contact_ID FROM appointments\n" +
                "JOIN contacts ON contacts.Contact_ID = appointments.Contact_ID";

        try {
            DBQuery.setPreparedStatement(conn, selectStatement);

            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                appointmentList.add(new Appointments(rs.getInt("Appointment_ID"),
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
