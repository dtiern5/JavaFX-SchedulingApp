package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Country;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCustomers {

    public static Customer getCustomer(int customerId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT * FROM customers WHERE Customer_ID = " + Integer.toString(customerId);

        DBQuery.setPreparedStatement(conn, selectStatement);

        Customer customerResult = null;

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int customerId1 = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");

            customerResult = new Customer(customerId1, customerName, address, postalCode, phone,
                    createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);

            return customerResult;
        }
        return null;
    }

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
                    rs.getString("Create_Date"),
                    rs.getString("Created_By"),
                    rs.getString("Last_Update"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Division_ID")));
        }
        return customerList;
    }

    public static ObservableList populateCustomerTable() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, first_level_divisions.Division, countries.Country, customers.Phone FROM customers\n" +
                "JOIN first_level_divisions ON customers.Division_ID=first_level_divisions.Division_ID\n" +
                "JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID";

        DBQuery.setPreparedStatement(conn, selectStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        while (rs.next()) {
            customerList.add(new Customer(rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Division"),
                    rs.getString("Country"),
                    rs.getString("Phone")));
        }
        return customerList;
    }

    public static void modifyCustomer(String customerName, String address, String postalCode, String phoneNumber, String currentUser, int divisionId, int customerId) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

        DBQuery.setPreparedStatement(conn, updateStatement);

        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNumber);
        ps.setString(5, currentUser.toString());
        ps.setInt(6, divisionId);
        ps.setInt(7, customerId);

        ps.execute();
    }


/*
    public static void addCustomer() {
        Connection conn = DBConnection.getConnection();

        String insertStatement = "INSERT INTO"

    }
*/

}
