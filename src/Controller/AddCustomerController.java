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
 * Controller for the AddCustomer screen
 */
public class AddCustomerController implements Initializable {

    @FXML
    private Label feedbackLabel;
    @FXML
    ComboBox<User> userCombo;

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
     * Populates the countryComboBox with available countries. Populates the table with customers.
     *
     * @param url            the location used to resolve relative paths for the root object
     * @param resourceBundle resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize countryComboBox
        try {
            ObservableList<Country> countryList = DBCountries.getAllACountries();
            countryComboBox.setItems(countryList);
            countryComboBox.setPromptText("Select Country");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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


        populateTableView();
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
     * Creates and sets a list for the divisionComboBox based on the countryComboBox selection
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
     * Ensures no fields are empty.
     * Adds customer to database.
     *
     * @param event for confirming update on click
     */
    public void confirmHandler(ActionEvent event) {
        Connection conn = DBConnection.getConnection();

        try {
            if (nameTF.getText().isEmpty() ||
                    addressTF.getText().isEmpty() ||
                    postalCodeTF.getText().isEmpty() ||
                    phoneNumberTF.getText().isEmpty() ||
                    countryComboBox.getSelectionModel().isEmpty() ||
                    divisionComboBox.getSelectionModel().isEmpty()) {
                feedbackLabel.setText("Error: All fields require values");
                feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));
            } else {

                String customerName = nameTF.getText();
                String address = addressTF.getText();
                String postalCode = postalCodeTF.getText();
                String phoneNumber = phoneNumberTF.getText();
                String userString = userCombo.getValue().toString();
                int divisionId = divisionComboBox.getValue().getDivisionId();

                DBCustomers.addCustomer(customerName, address, postalCode, phoneNumber, userString, divisionId);

                feedbackLabel.setText("Customer '" + customerName + "' added");
                feedbackLabel.setTextFill(Color.color(0.2, 0.6, 0.2));
                clearData();

                populateTableView();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error");
            alert.setContentText("Customer Not Saved");
            alert.showAndWait();
        }
    }

    private void clearData() {
        customerIdTF.clear();
        nameTF.clear();
        addressTF.clear();
        postalCodeTF.clear();
        countryComboBox.valueProperty().set(null);
        phoneNumberTF.clear();
        userCombo.valueProperty().set(null);
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
