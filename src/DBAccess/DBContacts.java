package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database access class for Contacts
 */
public class DBContacts {

    /**
     * Returns a contact by the given contact ID.
     *
     * @param contactId the contact ID used for retrieval
     * @return the contact being returned
     * @throws SQLException signals SQL Exception has occurred
     */
    public static Contact getContact(int contactId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM contacts WHERE Contact_ID = " + contactId;

        DBQuery.setPreparedStatement(conn, selectStatement);

        Contact contactResult;

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId1 = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String email = rs.getString("Email");

            contactResult = new Contact(contactId1, contactName, email);

            return contactResult;
        }
        return null;
    }

    /**
     * Returns an ObservableList of all contacts in the database.
     *
     * @return ObservableList of all contacts
     * @throws SQLException signals SQL Exception has occurred
     */
    public static ObservableList getAllAContacts() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM contacts";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        while (rs.next()) {
            contactList.add(new Contact(rs.getInt("Contact_ID"),
                    rs.getString("Contact_Name"),
                    rs.getString("Email")));
        }
        return contactList;
    }
}
