package Controller;

import DBAccess.DBAppointments;
import DBAccess.DBCustomers;
import Model.Appointment;
import Model.Customer;
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
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the main screen
 */
public class MainScreenController implements Initializable {

    public User currentUser;

    @FXML
    private TableView<Customer> customerTableView;
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

    @FXML
    private TableView<Appointment> appointmentsTableView;
    @FXML
    private TableColumn<Appointment, Integer> appointmentsIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsTitleColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsDescriptionColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsLocationColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsContactColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsTypeColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsStartTimeColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsEndTimeColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentsCustomerIdColumn;

    @FXML
    private Label userLabel;


    public void initData(User user) {
        currentUser = user;
        userLabel.setText("Current user: " + currentUser);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        ObservableList<Customer> customerList = null;
        ObservableList<Appointment> appointmentList = null;


        try {
            customerList = DBCustomers.populateCustomerTable();
            customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
            customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
            customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            customerTableView.setItems(customerList);


            appointmentList = DBAppointments.populateAppointmentsTable();
            appointmentsIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            appointmentsTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            appointmentsDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            appointmentsLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            appointmentsContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
            appointmentsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            appointmentsStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            appointmentsEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            appointmentsCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            appointmentsTableView.setItems(appointmentList);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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

    /**
     * Switches to AddCustomer scene.
     *
     * @param event for changing scene on click
     * @throws IOException signals I/O exception has occurred
     */
    public void addCustomerHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("../View/AddCustomerView.fxml"));
        Parent scene = loader.load();
        Scene customerViewScene = new Scene(scene);

        AddCustomerController controller = loader.getController();
        controller.initData(currentUser);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(customerViewScene);
        window.show();
    }

    public void addAppointmentHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("../View/AddAppointmentView.fxml"));
        Parent scene = loader.load();
        Scene appointmentViewScene = new Scene(scene);

        AddAppointmentController controller = loader.getController();
        controller.initData(currentUser);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(appointmentViewScene);
        window.show();
    }

    public void modifyCustomerHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("../View/ModifyCustomerView.fxml"));
        Parent scene = loader.load();
        Scene modifyCustomerScene = new Scene(scene);

        ModifyCustomerController controller = loader.getController();
        controller.initData(currentUser);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(modifyCustomerScene);
        window.show();

    }


}
