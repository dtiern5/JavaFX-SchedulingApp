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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the main screen
 */
public class MainScreenController implements Initializable {

    @FXML
    private Label feedbackLabel;

    @FXML
    private DatePicker datePicker;
    @FXML
    private RadioButton allRadio;
    @FXML
    private RadioButton monthlyRadio;
    @FXML
    private RadioButton weeklyRadio;
    @FXML
    private ToggleGroup calendarViewToggleGroup;


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

    private User currentUser;


    // TODO: watch lambda webinar
    /**
     * Populates the customer table and the appointment table.
     *
     * @param url            the location used to resolve relative paths for the root object
     * @param resourceBundle resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate tableviews
        try {
            populateAppointmentTable();
            populateCustomerTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        datePicker.setValue(LocalDate.now());

        //Add toggle group for radio buttons
        calendarViewToggleGroup = new ToggleGroup();
        this.allRadio.setToggleGroup(calendarViewToggleGroup);
        this.monthlyRadio.setToggleGroup(calendarViewToggleGroup);
        this.weeklyRadio.setToggleGroup(calendarViewToggleGroup);
        this.allRadio.setSelected(true);
    }

    /**
     * Switches appointment table to show appointments by month, by week, or all appointments
     * given the selected datePicker value.
     *
     * @throws SQLException signals SQL Exception has occurred
     */
    public void radioButtonChanged() throws SQLException {
        if (calendarViewToggleGroup.getSelectedToggle().equals(allRadio)) {
            populateAppointmentTable();
        } else if (calendarViewToggleGroup.getSelectedToggle().equals(monthlyRadio)) {
            int year = datePicker.getValue().getYear();
            int month = datePicker.getValue().getMonthValue();
            populateMonthlyAppointmentTable(year, month);
        } else {
            LocalDate date = datePicker.getValue();
            populateWeeklyAppointmentTable(date);
        }
    }

    /**
     * Displays all appointments in the appointment table.
     *
     * @throws SQLException signals SQL Exception has occurred
     */
    private void populateAppointmentTable() throws SQLException {
        ObservableList<Appointment> appointmentList = null;

        appointmentList = DBAppointments.getAllAppointments();
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
    }

    /**
     * Displays appointments of the selected month in the appointment table.
     *
     * @throws SQLException signals SQL Exception has occurred
     */
    private void populateMonthlyAppointmentTable(int year, int month) throws SQLException {
        ObservableList<Appointment> appointmentList = null;

        appointmentList = DBAppointments.getAllAppointmentsByMonth(year, month);
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
    }

    /**
     * Displays appointments of the selected week in the appointment table.
     *
     * @throws SQLException signals SQL Exception has occurred
     */
    private void populateWeeklyAppointmentTable(LocalDate date) throws SQLException {
        ObservableList<Appointment> appointmentList = null;

        appointmentList = DBAppointments.getAllAppointmentsByWeek(date);
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
    }

    /**
     * Displays all customers in the customer table.
     *
     * @throws SQLException signals SQL Exception has occurred
     */
    private void populateCustomerTable() throws SQLException {
        ObservableList<Customer> customerList = null;

        customerList = DBCustomers.getAllCustomers();
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerTableView.setItems(customerList);

    }

    /**
     * Switches to AddCustomer screen.
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

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(customerViewScene);
        window.show();
    }

    /**
     * Switches to the AddAppointment screen.
     *
     * @param event for changing scene on click
     * @throws IOException signals I/O exception has occurred
     */
    public void addAppointmentHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("../View/AddAppointmentView.fxml"));
        Parent scene = loader.load();
        Scene appointmentViewScene = new Scene(scene);

        AddAppointmentController controller = loader.getController();

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(appointmentViewScene);
        window.show();
    }

    /**
     * Switches to the ModifyCustomer screen. If a customer is selected in the table, it will be sent
     * to initialize the ModifyCustomer screen.
     *
     * @param event for changing scene on click
     * @throws IOException signals I/O exception has occurred
     */
    public void modifyCustomerHandler(ActionEvent event) throws IOException {
        if (customerTableView.getSelectionModel().isEmpty()) {
            feedbackLabel.setText("Must select customer to modify");
            feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));
        } else {
            FXMLLoader loader = new FXMLLoader();


            loader.setLocation(getClass().getResource("../View/ModifyCustomerView.fxml"));
            Parent scene = loader.load();
            Scene modifyCustomerScene = new Scene(scene);

            ModifyCustomerController controller = loader.getController();
            controller.initData(customerTableView.getSelectionModel().getSelectedItem());

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(modifyCustomerScene);
            window.show();
        }

    }

    /**
     * Switches to the ModifyAppointment screen. If an appointment is selected in the table, it will be sent
     * to initialize the ModifyAppointment screen.
     *
     * @param event for changing scene on click
     * @throws IOException signals I/O exception has occurred
     */
    public void modifyAppointmentHandler(ActionEvent event) throws IOException, SQLException {
        if (appointmentsTableView.getSelectionModel().isEmpty()) {
            feedbackLabel.setText("Must select appointment to modify");
            feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));
        } else {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("../View/ModifyAppointmentView.fxml"));
            Parent scene = loader.load();
            Scene modifyAppointmentScene = new Scene(scene);

            ModifyAppointmentController controller = loader.getController();
            controller.initData(appointmentsTableView.getSelectionModel().getSelectedItem());

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(modifyAppointmentScene);
            window.show();
        }

    }

    // TODO: Rewrite this to not be such a mess of if/else statements
    /**
     * Checks if customer has any scheduled appointments and shows an error if so.
     * Else, confirms the deletion of selected customer from the database.
     *
     * @param event for deleting customer on click
     * @throws SQLException signals SQL Exception has occurred
     */
    public void deleteCustomerHandler(ActionEvent event) throws SQLException {

        if (customerTableView.getSelectionModel().isEmpty()) {
            feedbackLabel.setText("Must select customer to delete");
            feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));
        } else {
            Customer customer = customerTableView.getSelectionModel().getSelectedItem();
            ObservableList<Appointment> appointmentList = null;

            appointmentList = DBAppointments.getAppointmentsByCustomerID(customer.getCustomerId());

            if (appointmentList.size() > 0) {
                feedbackLabel.setText("Customer not deleted");
                feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));
                int appointmentCount = appointmentList.size();
                Alert ErrorAlert = new Alert(Alert.AlertType.ERROR);
                ErrorAlert.setHeaderText("Cannot delete customer");
                ErrorAlert.setContentText("Customer has " + appointmentCount + " scheduled appointments");
                ErrorAlert.showAndWait();
            } else {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete?");
                alert.setHeaderText("Customer ID: " + customer.getCustomerId());
                Optional<ButtonType> result = alert.showAndWait();

                if (appointmentList.size() == 0 && result.isPresent() && result.get() == ButtonType.OK) {
                    feedbackLabel.setText("Customer ID " + customer.getCustomerId() + " deleted");
                    feedbackLabel.setTextFill(Color.color(0.2, 0.6, 0.2));
                    DBCustomers.deleteCustomer(customer.getCustomerId());
                    populateCustomerTable();
                } else {
                    feedbackLabel.setText("Customer not deleted");
                    feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));
                }
            }
        }
    }

    // TODO: Rewrite this to not be such a mess of if/else statements
    /**
     * Confirms the deletion of selected appointment from the database.
     *
     * @param event for deleting customer on click
     * @throws SQLException signals SQL Exception has occurred
     */
    public void deleteAppointmentHandler(ActionEvent event) throws SQLException {
        if (appointmentsTableView.getSelectionModel().isEmpty()) {
            feedbackLabel.setText("Must select appointment to delete");
            feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));
        } else {

            Appointment appointment = appointmentsTableView.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete?");
            alert.setHeaderText("Appointment ID: " + appointment.getAppointmentId());
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                feedbackLabel.setText("Appointment ID " + appointment.getAppointmentId() + " of type "
                + appointment.getType() + " deleted");
                feedbackLabel.setTextFill(Color.color(0.2, 0.6, 0.2));

                DBAppointments.deleteAppointment(appointment.getAppointmentId());
                populateAppointmentTable();
            } else {
                feedbackLabel.setText("Appointment not deleted");
                feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));
            }
        }
    }

    /**
     * Switches to the typeReport screen for displaying the count of appointments
     * by type and month.
     *
     * @param event for changing scene on click
     * @throws IOException signals I/O exception has occurred
     */
    public void typeReportHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("../View/AppTypeReportView.fxml"));
        Parent scene = loader.load();
        Scene reportAppointmentsScene = new Scene(scene);

        AppTypeReportController controller = loader.getController();

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(reportAppointmentsScene);
        window.show();
    }

    /**
     * Switches to the contactReport screen for displaying a contact's schedule.
     *
     * @param event for changing scene on click
     * @throws IOException signals I/O exception has occurred
     */
    public void contactReportHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("../View/ContactSchedReportView.fxml"));
        Parent scene = loader.load();
        Scene contactReportScene = new Scene(scene);

        ContactSchedReportController controller = loader.getController();

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(contactReportScene);
        window.show();
    }

    /**
     * Switches to the locationReport screen for displaying the number of customers by first level division.
     *
     * @param event for changing scene on click
     * @throws IOException signals I/O exception has occurred
     */
    public void locationReportHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("../View/CustomerLocationReportView.fxml"));
        Parent scene = loader.load();
        Scene locationReportScene = new Scene(scene);

        CustomerLocationReportController controller = loader.getController();

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(locationReportScene);
        window.show();
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
