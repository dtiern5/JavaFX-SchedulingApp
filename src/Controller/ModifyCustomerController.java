package Controller;

import DBAccess.DBCountries;
import DBAccess.DBCustomers;
import DBAccess.DBDivisions;
import Database.DBConnection;
import Database.DBQuery;
import Model.Country;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {

    public User currentUser;
    private Customer currentCustomer;

    @FXML
    private Label userLabel;
    @FXML
    private Label currentCustomerLabel;

    @FXML
    private TextField customerIdTF;
    @FXML
    private TextField nameTF;
    @FXML
    private TextField addressTF;
    @FXML
    private TextField postalCodeTF;
    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private ComboBox<Division> divisionComboBox;
    @FXML
    private TextField phoneNumberTF;

    @FXML
    private TableView<Customer> customersTableView;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerAddressColumn;
    @FXML
    private TableColumn<Customer, String> customerPostalCodeColumn;
    @FXML
    private TableColumn<Customer, String> customerDivisionColumn;
    @FXML
    private TableColumn<Customer, String> customerCountryColumn;
    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;

    public void initData(User user, Customer customer) {
        currentUser = user;
        userLabel.setText("Current user: " + currentUser);
        currentCustomer = customer;
        if (customer != null) {
            disableTableView();
            enableFields();
            currentCustomer = customer;
            customerIdTF.setText(String.valueOf(currentCustomer.getCustomerId()));
            nameTF.setText(currentCustomer.getCustomerName());
            addressTF.setText(currentCustomer.getAddress());
            postalCodeTF.setText(currentCustomer.getPostalCode());
            countryComboBox.getSelectionModel().select(currentCustomer.getCountry());

            ObservableList<Division> divisionList = null;
            try {
                divisionList = DBDivisions.getDivisionByCountryId(countryComboBox.getValue().getCountryID());
                divisionComboBox.setItems(divisionList);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            divisionComboBox.getSelectionModel().select(currentCustomer.getDivision());
            phoneNumberTF.setText(currentCustomer.getPhone());

            currentCustomerLabel.setText("Customer: " + currentCustomer.getCustomerId() + ", " + currentCustomer.getCustomerName());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Country> countryList = DBCountries.getAllACountries();
            countryComboBox.setItems(countryList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        disableFields();
        populateTableView();
    }

    private void populateTableView() {
        ObservableList<Customer> customerList = null;
        try {
            customerList = DBCustomers.populateCustomerTable();
            customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
            customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
            customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            customersTableView.setItems(customerList);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void divisionHandler(ActionEvent event) {
        if (countryComboBox.getSelectionModel().isEmpty()) {
            divisionComboBox.getSelectionModel().select(null);
        } else {
            try {
                ObservableList<Division> divisionList = DBDivisions.getDivisionByCountryId(countryComboBox.getValue().getCountryID());
                divisionComboBox.setItems(divisionList);
                divisionComboBox.getSelectionModel().select(0);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void selectHandler(ActionEvent event) {
        if (customersTableView.getSelectionModel().isEmpty()) {
            //do nothing
        } else {
            disableTableView();
            enableFields();
            populateFields();
        }
    }

    public void clearHandler(ActionEvent actionEvent) {

        customersTableView.getSelectionModel().clearSelection();
        customerIdTF.clear();
        nameTF.clear();
        addressTF.clear();
        postalCodeTF.clear();
        countryComboBox.valueProperty().set(null);
        phoneNumberTF.clear();

        currentCustomer = null;
        currentCustomerLabel.setText("No customer selected");

        customersTableView.setDisable(false);
        disableFields();
    }

    public void populateFields() {
        if (customersTableView.getSelectionModel().isEmpty()) {
            currentCustomerLabel.setText("No customer selected");
        } else {
            currentCustomer = customersTableView.getSelectionModel().getSelectedItem();
            customerIdTF.setText(String.valueOf(currentCustomer.getCustomerId()));
            nameTF.setText(currentCustomer.getCustomerName());
            addressTF.setText(currentCustomer.getAddress());
            postalCodeTF.setText(currentCustomer.getPostalCode());
            countryComboBox.getSelectionModel().select(currentCustomer.getCountry());

            ObservableList<Division> divisionList = null;
            try {
                divisionList = DBDivisions.getDivisionByCountryId(countryComboBox.getValue().getCountryID());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            divisionComboBox.setItems(divisionList);
            divisionComboBox.getSelectionModel().select(currentCustomer.getDivision());
            phoneNumberTF.setText(currentCustomer.getPhone());

            currentCustomerLabel.setText("Customer: " + currentCustomer.getCustomerId() + ", " + currentCustomer.getCustomerName());
        }
    }

    public void disableTableView() {
        customersTableView.setDisable(true);
    }

    public void disableFields() {
        customerIdTF.setDisable(true);
        nameTF.setDisable(true);
        addressTF.setDisable(true);
        postalCodeTF.setDisable(true);
        countryComboBox.setDisable(true);
        divisionComboBox.setDisable(true);
        phoneNumberTF.setDisable(true);
    }

    public void enableFields() {
        customerIdTF.setDisable(false);
        nameTF.setDisable(false);
        addressTF.setDisable(false);
        postalCodeTF.setDisable(false);
        countryComboBox.setDisable(false);
        divisionComboBox.setDisable(false);
        phoneNumberTF.setDisable(false);
    }

    /**
     * @param event for inserting new customer into the database
     */
    public void confirmHandler(ActionEvent event) {
        Connection conn = DBConnection.getConnection();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {
            alert.setTitle("Customer not added");

            if (nameTF.getText().isEmpty() ||
                    addressTF.getText().isEmpty() ||
                    postalCodeTF.getText().isEmpty() ||
                    phoneNumberTF.getText().isEmpty() ||
                    countryComboBox.isShowing() ||
                    divisionComboBox.isShowing()) {
                alert.setContentText("All fields require values");
                alert.showAndWait();
            } else {

                String customerName = nameTF.getText();
                String address = addressTF.getText();
                String postalCode = postalCodeTF.getText();
                String phoneNumber = phoneNumberTF.getText();
                int divisionId = divisionComboBox.getValue().getDivisionId();
                int customerId = Integer.valueOf(customerIdTF.getText());

                DBCustomers.modifyCustomer(customerName, address, postalCode, phoneNumber, currentUser.toString(), divisionId, customerId);

                populateTableView();
                System.out.println("Success");

                clearHandler(null);
            }

        } catch (Exception e) {
            System.out.println("Input error: " + e.getMessage());
            alert.setTitle("Input error");
            alert.setContentText("Customer Not Saved");
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
