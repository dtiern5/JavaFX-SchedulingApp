package Controller;

import DBAccess.DBCountries;
import DBAccess.DBDivisions;
import Database.DBConnection;
import Database.DBQuery;
import Model.Customer;
import Model.Division;
import Model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {

    public User currentUser;
    public Customer customer;

    @FXML
    private Label userLabel;

    @FXML
    private TextField customerIdTF;
    @FXML
    private TextField nameTF;
    @FXML
    private TextField addressTF;
    @FXML
    private TextField postalCodeTF;
    @FXML
    private ComboBox<Division> divisionComboBox;
    @FXML
    private TextField countryTF;
    @FXML
    private TextField phoneNumberTF;

    public void initData(User user, Customer customer) {
        currentUser = user;
        userLabel.setText("Current user: " + currentUser);
        this.customer = customer;

        try {
            ObservableList<Division> divisionList = DBDivisions.getAllDivisions();
            divisionComboBox.setItems(divisionList);
            divisionComboBox.setPromptText("First Level Division");

            customerIdTF.setText(String.valueOf(customer.getCustomerId()));
            nameTF.setText(customer.getCustomerName());
            addressTF.setText(customer.getAddress());
            postalCodeTF.setText(customer.getPostalCode());

            divisionComboBox.setValue(DBDivisions.getDivision(customer.getDivisionId()));

            // countryTF.setText(DBCountries.getCountry(customer.getDivisionId()).toString());

            phoneNumberTF.setText(customer.getPhone());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



    /**
     * On changing the division ComboBox, the getCountry method will be called on
     * the division's countryId. The toString() method will be called on that country
     * and displayed in the countryTF.
     *
     * @param event for changing country to match division
     * @throws SQLException signals SQLException has occurred
     */
    public void countryHandler(ActionEvent event) throws SQLException {
        countryTF.setText(DBCountries.getCountry(divisionComboBox.getValue().getCountryId()).toString());
    }


    /**
     *
     *
     * @param event for inserting new customer into the database
     */
    public void confirmHandler(ActionEvent event) {
        Connection conn = DBConnection.getConnection();

        try {


            if (nameTF.getText().isEmpty() ||
                    addressTF.getText().isEmpty() ||
                    postalCodeTF.getText().isEmpty() ||
                    phoneNumberTF.getText().isEmpty() ||
                    divisionComboBox.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Customer not modified");
                alert.setContentText("All fields require values");
                alert.showAndWait();
            }

            String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

            DBQuery.setPreparedStatement(conn, updateStatement);

            PreparedStatement ps = DBQuery.getPreparedStatement();

            String customerName = nameTF.getText();
            String address = addressTF.getText();
            String postalCode = postalCodeTF.getText();
            String phoneNumber = phoneNumberTF.getText();
            int divisionId = divisionComboBox.getValue().getDivisionId();
            int customerId = Integer.valueOf(customerIdTF.getText());

            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setString(5, currentUser.toString());
            ps.setInt(6, divisionId);
            ps.setInt(7, customerId);

            ps.execute();
            System.out.println("Success");

        } catch (Exception e) {
            System.out.println("Input error : " + e.getMessage());
        }
    }


    /**
     * Reverts back to main screen
     *
     * @param event for changing scene on click
     * @throws IOException signals I/O exception has occurred
     */
    public void goBackHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to go back?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("../View/MainScreenView.fxml"));
            Parent scene = loader.load();
            Scene mainViewScene = new Scene(scene);

            MainScreenController controller = loader.getController();
            controller.initData(currentUser);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainViewScene);
            window.show();
        }
    }

}
