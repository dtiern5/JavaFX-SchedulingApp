package View;

import DBAccess.DBCustomers;
import Database.DBConnection;
import Database.DBQuery;
import Model.Appointments;
import Model.Customers;
import javafx.collections.FXCollections;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the main screen
 */
public class MainScreenController implements Initializable {

    @FXML
    private TableView<Customers> customersTableView;
    @FXML
    private TableColumn<Customers, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customers, String> customerNameColumn;
    @FXML
    private TableColumn<Customers, String> customerAddressColumn;
    @FXML
    private TableColumn<Customers, String> customerDivisionColumn;
    @FXML
    private TableColumn<Customers, String> customerCountryColumn;
    @FXML
    private TableColumn<Customers, String> customerPhoneColumn;

    @FXML
    private TableView<Appointments> appointmentsTableView;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsIdColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsTitleColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsLocationColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsDescriptionColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsStartTimeColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsEndTimeColumn;

    ObservableList<Customers> customerList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerList = DBCustomers.getCustomers();

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        /*
        appointmentsIdColumn.setCellFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentsTitleColumn.setCellFactory(new PropertyValueFactory<>("title"));
        appointmentsLocationColumn.setCellFactory(new PropertyValueFactory<>("location"));
        appointmentsDescriptionColumn.setCellFactory(new PropertyValueFactory<>("description"));
        appointmentsStartTimeColumn.setCellFactory(new PropertyValueFactory<>("startTime"));
        appointmentsEndTimeColumn.setCellFactory(new PropertyValueFactory<>("endTime"));
        */

        customersTableView.setItems(customerList);

    }


    /**
     * Reverts back to log in screen.
     *
     * @param event for changing scene on click
     * @throws IOException signals I/O exception has occurred
     */
    public void logOutHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            Parent loginScreenViewParent = FXMLLoader.load(getClass().getResource("../View/LogInView.fxml"));
            Scene mainViewScene = new Scene(loginScreenViewParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainViewScene);
            window.show();
        }
    }


}
