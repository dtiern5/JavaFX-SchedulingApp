package Controller;

import BundlesAndUtilities.TimeConversions;
import DBAccess.*;
import Model.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Controller for the AddAppointment screen.
 */
public class AddAppointmentController implements Initializable {

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

    private ObservableList<Appointment> appointmentList;


    /**
     * Populates ComboBoxes for contacts, customers, start times, and end times.
     *
     * @param url            the location used to resolve relative paths for the root object
     * @param resourceBundle resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        populateCustomerCombo();
        populateContactCombo();
        populateStartCombo();
        populateUserCombo();
        populateAppointments();

        datePicker.setValue(LocalDate.now());

        startTimeCombo.setPromptText("Select start time");
        customerCombo.setPromptText("Select customer");
        contactCombo.setPromptText("Select contact");
        userCombo.setPromptText("Select user");
    }

    private void populateAppointments() {
        try {
            appointmentList = DBAppointments.getAllAppointments();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

        Callback<ListView<Customer>, ListCell<Customer>> customerFactory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty ? "" : customer.toStringWithId());
            }
        };

        Callback<ListView<Customer>, ListCell<Customer>> factorySelected = lv -> new ListCell<>() {
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

        Callback<ListView<User>, ListCell<User>> userFactory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "NOTHING" : user.toStringWithId());
            }
        };

        Callback<ListView<User>, ListCell<User>> userFactorySelected = lv -> new ListCell<>() {
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
     * Creates and sets a list for the endTimeCombo of possible end times (between 15 after start time and 22:00 EST)
     */
    public void endTimeHandler() {
        endTimeCombo.getItems().clear();

        LocalTime availableEndTime = startTimeCombo.getValue().plusMinutes(15);
        LocalTime lastTimeEst = LocalTime.of(22, 0);

        LocalDateTime lastTimeLocal = TimeConversions.fromEstToLocal(LocalDateTime.of(LocalDate.now(), lastTimeEst));

        while (availableEndTime.isBefore(LocalTime.from(lastTimeLocal.plusSeconds(1)))) {
            endTimeCombo.getItems().add(availableEndTime);
            availableEndTime = availableEndTime.plusMinutes(15);
        }
        endTimeCombo.getSelectionModel().select(0);
    }

    /**
     * Ensures no fields are empty, selected date is a weekday, start and end times are valid, and customers are not
     * scheduled for overlapping appointments.
     * Then, saves the appointment to the database.
     */
    public void confirmHandler() {

        if (datePicker.getValue() == null ||
                startTimeCombo.getValue() == null ||
                endTimeCombo.getValue() == null) {
            feedbackLabel.setText("Error: All fields require values");
            feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));
            return;
        }

        // TODO: Can move this time conversion to a separate method
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


        try {
            if (titleTF.getText().isEmpty() ||
                    descriptionTF.getText().isEmpty() ||
                    locationTF.getText().isEmpty() ||
                    typeTF.getText().isEmpty() ||
                    userCombo.getValue() == null ||
                    contactCombo.getValue() == null ||
                    customerCombo.getValue() == null) {

                feedbackLabel.setText("Error: All fields require values");
                feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));

            } else if (datePicker.getValue().getDayOfWeek() == DayOfWeek.SATURDAY ||
                    datePicker.getValue().getDayOfWeek() == DayOfWeek.SUNDAY) {
                feedbackLabel.setText("Error: Cannot schedule appointment on weekend");
                feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));

            } else if (convertedStartLdt.isBefore(estStartLdt) ||
                    convertedStartLdt.isAfter(estEndLdt) ||
                    convertedEndLdt.isBefore(convertedStartLdt) ||
                    convertedEndLdt.isAfter(estEndLdt)) {
                feedbackLabel.setText("Error: Valid hours are between 8AM and 10PM EST");
                feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));

            } else if (overlappingAppointment()) {
                feedbackLabel.setText("Error: Customer has overlapping appointment");
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

                DBAppointments.addAppointment(title, description, location, type, start, end, userCombo.getValue().toString(), customerId, userId, contactId);

                feedbackLabel.setText("Appointment added");
                feedbackLabel.setTextFill(Color.color(0.2, 0.6, 0.2));
                populateAppointments();
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
     * Logic for checking for potential overlapping appointments
     * <p>
     * LAMBDA: The lambda expression filters the list of all appointments to only include appointments associated with
     * the selected customer. It increases efficiency by allowing us to use one list to filter from, instead
     * of accessing the database to create a new list for each customer.
     * </p>
     * @return a boolean true for an overlapping appointment, and false for no overlap
     */
    private boolean overlappingAppointment() {
        LocalDate chosenDate = datePicker.getValue();
        LocalTime startTime = startTimeCombo.getValue();
        LocalTime endTime = endTimeCombo.getValue();
        LocalDateTime start = LocalDateTime.of(chosenDate, startTime);
        LocalDateTime end = LocalDateTime.of(chosenDate, endTime);

        Customer selectedCustomer = customerCombo.getValue();

        FilteredList<Appointment> customerAppointments = new FilteredList<>(appointmentList);

        // check if the contact for the appointment equals the selected contact
        customerAppointments.setPredicate(a -> a.getCustomerId() == selectedCustomer.getCustomerId());

        boolean overlappingAppointment = false;

        for (Appointment a : customerAppointments) {
            if (a.getStartTime().isAfter(start) && a.getStartTime().isBefore(end)) {
                overlappingAppointment = true;
            } else if (a.getStartTime().equals(start) || a.getStartTime().equals(end) || a.getEndTime().equals(start) || a.getEndTime().equals(end)) {
                overlappingAppointment = true;
            } else if (a.getEndTime().isAfter(start) && a.getEndTime().isBefore(end)) {
                overlappingAppointment = true;
            } else if (a.getStartTime().isBefore(start) && a.getEndTime().isAfter(end)) {
                overlappingAppointment = true;
            }
        }
        return overlappingAppointment;
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

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainViewScene);
            window.show();
        }
    }

}
