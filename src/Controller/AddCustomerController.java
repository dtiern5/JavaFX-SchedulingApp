package Controller;

import DBAccess.DBCountries;
import DBAccess.DBCustomers;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    public User currentUser;

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




    public void initData(User user) {
        currentUser = user;
        userLabel.setText("Current user: " + currentUser);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Division> divisionList = DBDivisions.getAllDivisions();
            divisionComboBox.setItems(divisionList);
            divisionComboBox.setPromptText("First Level Division");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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

        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {
            alert.setTitle("Customer not added");

            if (nameTF.getText().isEmpty() ||
                    addressTF.getText().isEmpty() ||
                    postalCodeTF.getText().isEmpty() ||
                    phoneNumberTF.getText().isEmpty() ||
                    divisionComboBox.getSelectionModel().isEmpty()) {
                alert.setContentText("All fields require values");
                alert.showAndWait();
            }

            String insertStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, " +
                    "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                    "VALUES(?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)";

            DBQuery.setPreparedStatement(conn, insertStatement);

            PreparedStatement ps = DBQuery.getPreparedStatement();

            String customerName = nameTF.getText();
            String address = addressTF.getText();
            String postalCode = postalCodeTF.getText();
            String phoneNumber = phoneNumberTF.getText();
            int divisionId = divisionComboBox.getValue().getDivisionId();

            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setString(5, currentUser.toString());
            ps.setString(6, currentUser.toString());
            ps.setInt(7, divisionId);

            ps.execute();

            populateTableView();


        } catch (Exception e) {
            System.out.println("Input error: " + e.getMessage());
            alert.setTitle("Input error");
            alert.setContentText("Placeholder");
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
