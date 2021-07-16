package Controller;

import DBAccess.DBCountries;
import DBAccess.DBCustomers;
import DBAccess.DBDivisions;
import DBAccess.DBUsers;
import Database.DBConnection;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the ModifyCustomer screen
 */
public class ModifyCustomerController implements Initializable {

    private Customer currentCustomer;

    @FXML
    private Label currentCustomerLabel;
    @FXML
    private Label feedbackLabel;
    @FXML
    private ComboBox<User> userCombo;

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

    /**
     * Accepts and displays the current user and a Customer's modifiable data.
     *
     * @param customer the customer to modify
     */
    public void initData(Customer customer) {

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
            userCombo.getSelectionModel().select(currentCustomer.getLastUpdatedBy());

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


    /**
     * Populates the countryComboBox with available countries. Populates the table with customers.
     *
     * @param url the location used to resolve relative paths for the root object
     * @param resourceBundle resources used to localize the root object
     */
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


        // Initialize userCombo
        ObservableList<User> userList = null;
        try {
            userList = DBUsers.getAllUsers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        userCombo.setItems(userList);

        Callback<ListView<User>, ListCell<User>> userFactory = lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "NOTHING" : user.toStringWithId());
            }
        };

        Callback<ListView<User>, ListCell<User>> factorySelected = lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : (user.toString()));
            }
        };
        userCombo.setCellFactory(userFactory);
        userCombo.setButtonCell(factorySelected.call(null));
        userCombo.getSelectionModel().select(0);
    }

    /**
     * Populates table with customers
     */
    private void populateTableView() {
        ObservableList<Customer> customerList = null;
        try {
            customerList = DBCustomers.getAllCustomers();
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

    /**
     * Creates and sets a list for the divisionComboBox based on the countryComboBox selection.
     *
     * @param event for limiting available divisions to selected country
     */
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

    /**
     * Populates fields with chosen customer data. Disables further tableview selections until selection is cleared
     * or customer data saved.
     *
     * @param event for selecting customer on click
     */
    public void selectHandler(ActionEvent event) {
        if (customersTableView.getSelectionModel().isEmpty()) {
            //do nothing
        } else {
            disableTableView();
            enableFields();
            populateFields();
        }
    }

    /**
     * Sets currentCustomer to null and clears their data from editing fields.
     *
     * @param actionEvent for clearing current selection on click
     */
    public void clearHandler(ActionEvent actionEvent) {

        customersTableView.getSelectionModel().clearSelection();
        customerIdTF.clear();
        nameTF.clear();
        addressTF.clear();
        postalCodeTF.clear();
        countryComboBox.valueProperty().set(null);
        phoneNumberTF.clear();
        userCombo.valueProperty().set(null);

        currentCustomer = null;
        currentCustomerLabel.setText("No customer selected");

        enableTableView();
        disableFields();
        feedbackLabel.setText("");
    }

    /**
     * Sets fields with currently selected customer's data.
     */
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
            userCombo.getSelectionModel().select(currentCustomer.getLastUpdatedBy());

            // TODO: userCombo is not updating

            // Initialize divisionComboBox. After this is set the divisionHandler can handle any changes.
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

    /**
     * Enables table access.
     */
    public void enableTableView() {
        customersTableView.setDisable(false);
    }

    /**
     * Disables table access.
     */
    public void disableTableView() {
        customersTableView.setDisable(true);
    }

    /**
     * Enables field and comboBox access.
     */
    public void enableFields() {
        customerIdTF.setDisable(false);
        nameTF.setDisable(false);
        addressTF.setDisable(false);
        postalCodeTF.setDisable(false);
        countryComboBox.setDisable(false);
        divisionComboBox.setDisable(false);
        phoneNumberTF.setDisable(false);
        userCombo.setDisable(false);
    }

    /**
     * Disables field and comboBox access.
     */
    public void disableFields() {
        customerIdTF.setDisable(true);
        nameTF.setDisable(true);
        addressTF.setDisable(true);
        postalCodeTF.setDisable(true);
        countryComboBox.setDisable(true);
        divisionComboBox.setDisable(true);
        phoneNumberTF.setDisable(true);
        userCombo.setDisable(true);
    }

    // TODO: Clean up logic for modifying customer
    /**
     * Ensures no fields are empty.
     * Updates customer in database.
     *
     * @param event for confirming update on click
     */
    public void confirmHandler(ActionEvent event) {
        if (currentCustomer == null) {
            return;
        }

        try {
            if (nameTF.getText().isEmpty() ||
                    addressTF.getText().isEmpty() ||
                    postalCodeTF.getText().isEmpty() ||
                    phoneNumberTF.getText().isEmpty() ||
                    countryComboBox.getValue() == null ||
                    divisionComboBox.getValue() == null ||
                    userCombo.getValue() == null) {
                feedbackLabel.setText("Error: All fields require values");
                feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));
            } else {

                String customerName = nameTF.getText();
                String address = addressTF.getText();
                String postalCode = postalCodeTF.getText();
                String phoneNumber = phoneNumberTF.getText();
                int divisionId = divisionComboBox.getValue().getDivisionId();
                int customerId = Integer.valueOf(customerIdTF.getText());
                String userString = userCombo.getValue().toString();

                DBCustomers.modifyCustomer(customerName, address, postalCode, phoneNumber, userString, divisionId, customerId);
                feedbackLabel.setText("Customer ID: " + customerId + " (" + customerName + ") updated");
                feedbackLabel.setTextFill(Color.color(0.2, 0.6, 0.2));


                populateTableView();
                System.out.println("Success");

                clearHandler(null);
            }

        } catch (Exception e) {
            System.out.println("Input error: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
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

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainViewScene);
            window.show();
        }
    }
}
