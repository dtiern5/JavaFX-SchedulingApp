package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Database access class for Customers
 */
public class DBCustomers {

    /**
     * Returns a Customer by the given Customer ID.
     *
     * @param customerId the Customer ID used for retrieval
     * @return the Customer being returned
     * @throws SQLException signals SQL Exception has occurred
     */
    public static Customer getCustomer(int customerId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM customers WHERE Customer_ID = " + customerId;

        DBQuery.setPreparedStatement(conn, selectStatement);

        Customer customerResult;

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");

            User createdByUser = DBUsers.getUserByName(createdBy);
            User lastUpdatedByUser = DBUsers.getUserByName(lastUpdatedBy);

            customerResult = new Customer(customerId, customerName, address, postalCode, phone,
                    createDate, createdByUser, lastUpdate, lastUpdatedByUser, divisionId);

            return customerResult;
        }
        return null;
    }

    /**
     * Returns an ObservableList of all Customers in the database.
     *
     * @return ObservableList of Customers
     * @throws SQLException signals SQL Exception has occurred
     */
    public static ObservableList getAllCustomers() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM customers";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        while (rs.next()) {
            customerList.add(new Customer(rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    rs.getTimestamp("Create_Date").toLocalDateTime(),
                    DBUsers.getUserByName(rs.getString("Created_By")),
                    rs.getTimestamp("Last_Update").toLocalDateTime(),
                    DBUsers.getUserByName(rs.getString("Last_Updated_By")),
                    rs.getInt("Division_ID")));
        }
        return customerList;
    }

    /**
     * Modifies a Customer in the database using the given parameters.
     *
     * @param customerName modified name
     * @param address modified address
     * @param postalCode modified postal code
     * @param phoneNumber modified phone number
     * @param userString string of the user modifying the customer
     * @param divisionId modified division ID
     * @param customerId modified customer ID
     * @throws SQLException signals SQL Exception has occurred
     */
    public static void modifyCustomer(String customerName, String address, String postalCode, String phoneNumber,
                                      String userString, int divisionId, int customerId) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                "Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? " +
                "WHERE Customer_ID = ?";

        DBQuery.setPreparedStatement(conn, updateStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNumber);
        ps.setString(5, userString);
        ps.setInt(6, divisionId);
        ps.setInt(7, customerId);

        ps.execute();
    }

    /**
     * Creates a customer in the database using the given parameters.
     *
     * @param customerName name of the customer
     * @param address address of the customer
     * @param postalCode postal code of the customer
     * @param phoneNumber phone number of the customer
     * @param userString string of the user adding the customer
     * @param divisionId division ID of the customer
     * @throws SQLException Signals a SQL Exception has occurred
     */
    public static void addCustomer(String customerName, String address, String postalCode, String phoneNumber,
                                   String userString, int divisionId) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String insertStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, " +
                "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                "VALUES(?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)";

        DBQuery.setPreparedStatement(conn, insertStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNumber);
        ps.setString(5, userString);
        ps.setString(6, userString);
        ps.setInt(7, divisionId);

        ps.execute();
    }

    /**
     * Deletes a Customer from the database using a given Customer ID.
     *
     * @param customerId the Customer ID indicating the Customer to be deleted
     * @throws SQLException signals a SQL Exception has occurred
     */
    public static void deleteCustomer(Integer customerId) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String deleteStatement = "DELETE FROM customers WHERE Customer_ID = " + customerId.toString();
        DBQuery.setPreparedStatement(conn, deleteStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
    }

}
