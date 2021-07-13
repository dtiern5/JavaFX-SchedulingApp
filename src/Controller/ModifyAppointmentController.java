package Controller;

import Bundles.TimeConversions;
import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import DBAccess.DBCustomers;
import DBAccess.DBUsers;
import Database.DBConnection;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyAppointmentController implements Initializable {

    public User currentUser;

    @FXML
    private Label userLabel;

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
    private ComboBox<Customer> customerIdCombo;
    @FXML
    private ComboBox<LocalTime> startTimeCombo;
    @FXML
    private ComboBox<LocalTime> endTimeCombo;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label feedbackLabel;
    @FXML
    private ComboBox<User> userIdCombo;

    private Appointment currentAppointment;

    /**
     * Accepts and displays the current user and an Appointment's modifiable data.
     *
     * @param user logged in user
     * @param appointment the appointment to modify
     */
    public void initData(User user, Appointment appointment) {
        currentUser = user;
        userLabel.setText("Current user: " + currentUser.getUserName());

        // Want to initialize the userCombo with the current user
        // Has to be in initData instead of initialize since we are passing in the user from the previous scene
        ObservableList<User> userList = null;
        try {
            userList = DBUsers.getAllUsers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        userIdCombo.setItems(userList);

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
        userIdCombo.setValue(currentUser);
        userIdCombo.setCellFactory(userFactory);
        userIdCombo.setButtonCell(factorySelected.call(null));

        if (appointment != null) {
            appointmentIdTF.setText(String.valueOf(appointment.getAppointmentId()));
            titleTF.setText(appointment.getTitle());
            descriptionTF.setText(appointment.getDescription());
            locationTF.setText(appointment.getLocation());
            typeTF.setText(appointment.getType());
            userIdCombo.getSelectionModel().select(appointment.getUserId());
            contactCombo.getSelectionModel().select(appointment.getContact());
            customerIdCombo.getSelectionModel().select(appointment.getCustomerId());
            datePicker.setValue(appointment.getStartTime().toLocalDate());
            startTimeCombo.getSelectionModel().select(appointment.getStartTime().toLocalTime());
            // TODO: See if SetValue will work, getSelectionModel is saying the fields are empty

            //  endTimeHandler will handle anything that happens after the window is open, but
            //  I can't find a better way to initialized the endTimeCombo with the available times
            //  Most of this code is copy-pasted from initialize method, but we can't initialize
            //  The end times until the start time is selected in this initData method.
            LocalTime estStartTime = LocalTime.of(8, 0);
            LocalTime estEndTime = LocalTime.of(22, 0);

            LocalDateTime start = TimeConversions.fromEstToLocal(LocalDateTime.of(LocalDate.now(), estStartTime));
            LocalDateTime end = TimeConversions.fromEstToLocal(LocalDateTime.of(LocalDate.now(), estEndTime));

            // Initialize endTimeCombo
            while (start.isBefore(end.minusSeconds(1))) {
                start = start.plusMinutes(15);
                endTimeCombo.getItems().add(LocalTime.from(start));
            }
            endTimeCombo.getSelectionModel().select(appointment.getEndTime().toLocalTime());

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate contact ComboBox
        ObservableList<Contact> contactList = null;
        try {
            contactList = DBContacts.getAllAContacts();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        contactCombo.setItems(contactList);


        // Populate customer ComboBox
        ObservableList<Customer> customerList = null;
        try {
            customerList = DBCustomers.getAllCustomers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        customerIdCombo.setItems(customerList);

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
        customerIdCombo.setCellFactory(customerFactory);
        customerIdCombo.setButtonCell(factorySelected.call(null));

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

    // TODO: this is copy-pasted from AddAppointmentController, need to fix logic
    public void confirmHandler(ActionEvent event) {
        Connection conn = DBConnection.getConnection();

        try {
            if (titleTF.getText().isEmpty() ||
                    descriptionTF.getText().isEmpty() ||
                    locationTF.getText().isEmpty() ||
                    typeTF.getText().isEmpty() ||
                    contactCombo.getSelectionModel().isEmpty() ||
                    customerIdCombo.getSelectionModel().isEmpty() ||
                    startTimeCombo.getSelectionModel().isEmpty() ||
                    endTimeCombo.getSelectionModel().isEmpty()) {
                feedbackLabel.setText("Error: All fields require values");
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

                int customerId = customerIdCombo.getValue().getCustomerId();
                int userId = userIdCombo.getValue().getUserId();
                int contactId = contactCombo.getValue().getContactId();
                int appointmentId = Integer.valueOf(appointmentIdTF.getText());

                DBAppointments.modifyAppointment(title, description, location, type, start, end, currentUser.toString(), customerId, userId, contactId, appointmentId);

                feedbackLabel.setText("Appointment added");
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
