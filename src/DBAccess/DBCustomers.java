package DBAccess;

import Database.DBConnection;
import Database.DBQuery;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCustomers {

    public static ObservableList populateCustomersTable() {

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        Connection conn = DBConnection.getConnection();
        String selectStatement = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, first_level_divisions.Division, countries.Country, customers.Phone FROM customers\n" +
                "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID\n" +
                "JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID";

        try {
            DBQuery.setPreparedStatement(conn, selectStatement);

            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                customerList.add(new Customer(rs.getInt("Customer_Id"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Division"),
                        rs.getString("Country"),
                        rs.getString("Phone")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }
}
