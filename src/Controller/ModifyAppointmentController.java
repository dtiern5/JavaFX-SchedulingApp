package Controller;

import BundlesAndUtilities.TimeConversions;
import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import DBAccess.DBCustomers;
import DBAccess.DBUsers;
import Model.Appointment;
import Model.Contact;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the ModifyAppointment screen
 */
public class ModifyAppointmentController implements Initializable {

    @FXML
    private TextField appointmentIdTF;
    @FXML
    private TextField titleTF;
    @FXML
    private TextField descriptionTF;
    @FXML
    private TextField locationTF;
    @FXML
    private TextField typeTF;
    @FXML
    private ComboBox<Contact> contactCombo;
    @FXML
    private ComboBox<Customer> customerCombo;
    @FXML
    private ComboBox<LocalTime> startTimeCombo;
    @FXML
    private ComboBox<LocalTime> endTimeCombo;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label feedbackLabel;
    @FXML
    private ComboBox<User> userCombo;

    private Appointment currentAppointment;

    /**
     * Accepts and displays the appointment's modifiable data.
     * Also populates the end time combo based on the given start time
     *
     * @param appointment the appointment to modify
     */
    public void initData(Appointment appointment) throws SQLException {
        currentAppointment = appointment;

        try {
            userCombo.setValue(DBUsers.getUser(currentAppointment.getUserId()));
            contactCombo.setValue(DBContacts.getContact(currentAppointment.getContactId()));
            customerCombo.setValue(DBCustomers.getCustomer(currentAppointment.getCustomerId()));
            appointmentIdTF.setText(String.valueOf(currentAppointment.getAppointmentId()));
            titleTF.setText(currentAppointment.getTitle());
            descriptionTF.setText(currentAppointment.getDescription());
            locationTF.setText(currentAppointment.getLocation());
            typeTF.setText(currentAppointment.getType());
            datePicker.setValue(currentAppointment.getStartTime().toLocalDate());
            startTimeCombo.getSelectionModel().select(currentAppointment.getStartTime().toLocalTime());
            populateEndCombo(); // Need the start time of appointment to populate the possible end times
            endTimeCombo.getSelectionModel().select(appointment.getEndTime().toLocalTime());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Populates ComboBoxes for contacts, customers, user, and start time
     *
     * @param url            the location used to resolve relative paths for the root object
     * @param resourceBundle resources used to localize the root object
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        populateCustomerCombo();
        populateContactCombo();
        populateStartCombo();
        populateUserCombo();

    }

    /**
     * Creates and sets a list for the endTimeCombo of possible end times (between 15 after start time and 22:00 EST)
     *
     * @param event for limiting end times to viable times
     */
    public void endTimeHandler(ActionEvent event) {
        endTimeCombo.getItems().clear();

        LocalTime start = startTimeCombo.getValue().plusMinutes(15);
        LocalTime estEndTime = LocalTime.of(22, 0);

        LocalDateTime end = TimeConversions.fromEstToLocal(LocalDateTime.of(LocalDate.now(), estEndTime));

        while (start.isBefore(LocalTime.from(end.plusSeconds(1)))) {
            endTimeCombo.getItems().add(start);
            start = start.plusMinutes(15);
            endTimeCombo.getSelectionModel().select(0);
        }

    }

    // TODO: add logic for scheduling overlapping appointments for customers
    /**
     * Ensures no fields are empty, selected date is a weekday, start and end times are valid, and customers are not
     * scheduled for overlapping appointments.
     * Then, saves the appointment to the database.
     *
     * @param event for adding appointment to the database on click.
     */
    public void confirmHandler(ActionEvent event) {
        if (datePicker.getValue() == null) {
            System.out.println("datepicker null");
            return;
        }

        LocalDate selectedDate = datePicker.getValue();

        // Start and end times for EST time zone
        LocalTime estStartTime = LocalTime.of(8, 0);
        LocalTime estEndTime = LocalTime.of(22, 0);

        // Start and end LDT for EST time zone
        // For comparing before/after logic
        LocalDateTime estStartLdt = LocalDateTime.of(selectedDate, estStartTime);
        LocalDateTime estEndLdt = LocalDateTime.of(selectedDate, estEndTime);

        // Get selected start and end times converted to EST for comparison logic
        LocalTime selectedStartTime = startTimeCombo.getValue();
        LocalDateTime selectedStartLdt = LocalDateTime.of(selectedDate, selectedStartTime);
        LocalDateTime convertedStartLdt = BundlesAndUtilities.TimeConversions.convertToEst(selectedStartLdt);

        LocalTime selectedEndTime = endTimeCombo.getValue();
        LocalDateTime selectedEndLdt = LocalDateTime.of(selectedDate, selectedEndTime);
        LocalDateTime convertedEndLdt = BundlesAndUtilities.TimeConversions.convertToEst(selectedEndLdt);

        // TODO: UNCOMMENT WEEKEND LOGIC WHEN DONE TESTING
        try {
            if (titleTF.getText().isEmpty() ||
                    descriptionTF.getText().isEmpty() ||
                    locationTF.getText().isEmpty() ||
                    typeTF.getText().isEmpty() ||
                    userCombo.getValue() == null ||
                    contactCombo.getValue() == null ||
                    customerCombo.getValue() == null ||
                    startTimeCombo.getValue() == null ||
                    endTimeCombo.getValue() == null ||
                    datePicker.getValue() == null) {

                feedbackLabel.setText("Error: All fields require values");
                feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));

            }/* else if (datePicker.getValue().getDayOfWeek() == DayOfWeek.SATURDAY ||
                    datePicker.getValue().getDayOfWeek() == DayOfWeek.SUNDAY
            ) {

                feedbackLabel.setText("Error: Cannot schedule appointment on weekend");
                feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));

            }*/ else if (convertedStartLdt.isBefore(estStartLdt) ||
                    convertedStartLdt.isAfter(estEndLdt) ||
                    convertedEndLdt.isBefore(convertedStartLdt) ||
                    convertedEndLdt.isAfter(estEndLdt)
            ) {

                feedbackLabel.setText("Error: Valid hours are between 8AM and 10PM EST");
                feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));

            } else {
                String title = titleTF.getText();
                String description = descriptionTF.getText();
                String location = locationTF.getText();
                String type = typeTF.getText();

                // Get the date and times, use those to make LocalDateTime objects for start and end
                LocalDate chosenDate = datePicker.getValue();
                LocalTime startTime = startTimeCombo.getValue();
                LocalTime endTime = endTimeCombo.getValue();
                LocalDateTime start = LocalDateTime.of(chosenDate, startTime);
                LocalDateTime end = LocalDateTime.of(chosenDate, endTime);

                int customerId = customerCombo.getValue().getCustomerId();
                int userId = userCombo.getValue().getUserId();
                int contactId = contactCombo.getValue().getContactId();
                int appointmentId = Integer.parseInt(appointmentIdTF.getText());
                String userString = DBUsers.getUser(userId).toString();

                DBAppointments.modifyAppointment(title, description, location, type, start, end, userString, customerId, userId, contactId, appointmentId);

                feedbackLabel.setText("Appointment updated");
                feedbackLabel.setTextFill(Color.color(0.2, 0.6, 0.2));

            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error");
            alert.setContentText("Appointment Not Saved");
            alert.showAndWait();
        }
    }

    /**
     * Sets the customer combo box with all customers in the database.
     */
    private void populateCustomerCombo() {
        // Populate customer ComboBox
        ObservableList<Customer> customerList = null;
        try {
            customerList = DBCustomers.getAllCustomers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        customerCombo.setItems(customerList);

        Callback<ListView<Customer>, ListCell<Customer>> customerFactory = lv -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty ? "" : customer.toStringWithId());
            }
        };

        Callback<ListView<Customer>, ListCell<Customer>> factorySelected = lv -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty ? "" : (customer.toString()));
            }
        };
        customerCombo.setCellFactory(customerFactory);
        customerCombo.setButtonCell(factorySelected.call(null));
    }

    /**
     * Sets the contact combo box with all contacts in the database.
     */
    private void populateContactCombo() {
        // Populate contact ComboBox
        ObservableList<Contact> contactList = null;
        try {
            contactList = DBContacts.getAllAContacts();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        contactCombo.setItems(contactList);
    }

    /**
     * Converts the available start times from EST to the local user's default.
     */
    private void populateStartCombo() {
        // Set available hours in EST time zone
        LocalTime estStartTime = LocalTime.of(8, 0);
        LocalTime estEndTime = LocalTime.of(22, 0);

        LocalDateTime start = TimeConversions.fromEstToLocal(LocalDateTime.of(LocalDate.now(), estStartTime));
        LocalDateTime end = TimeConversions.fromEstToLocal(LocalDateTime.of(LocalDate.now(), estEndTime));

        while (start.isBefore(end.minusSeconds(1))) {
            startTimeCombo.getItems().add(LocalTime.from(start));
            start = start.plusMinutes(15);
        }
    }

    /**
     * Converts the available end times from EST to the local user's default.
     * Only displays options after the selected start time.
     */
    private void populateEndCombo() {
        LocalTime availableEndTime = startTimeCombo.getValue().plusMinutes(15);
        LocalTime lastAvailableTime = LocalTime.of(22, 0);

        while (availableEndTime.isBefore(lastAvailableTime.plusSeconds(1))) {
            endTimeCombo.getItems().add(availableEndTime);
            availableEndTime = availableEndTime.plusMinutes(15);
            endTimeCombo.getSelectionModel().select(0);
        }

    }

    /**
     * Sets the user combo box with all users in the database.
     */
    private void populateUserCombo() {
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

        Callback<ListView<User>, ListCell<User>> userFactorySelected = lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : (user.toString()));
            }
        };

        userCombo.setCellFactory(userFactory);
        userCombo.setButtonCell(userFactorySelected.call(null));
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
