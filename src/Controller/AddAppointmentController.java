package Controller;

import Bundles.TimeConversions;
import DBAccess.*;
import Database.DBConnection;
import Model.*;
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
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;


/**
 * Controller for the AddAppointment screen
 */
public class AddAppointmentController<value> implements Initializable {

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


    /**
     * Populates ComboBoxes for contacts, customers, start times, and end times.
     *
     * @param url            the location used to resolve relative paths for the root object
     * @param resourceBundle resources used to localize the root object
     */
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
        contactCombo.setPromptText("Select Contact");


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
        customerCombo.setPromptText("Select customer");


        // Set available hours in EST time zone
        LocalTime estStartTime = LocalTime.of(8, 0);
        LocalTime estEndTime = LocalTime.of(22, 0);

        LocalDateTime start = TimeConversions.fromEstToLocal(LocalDateTime.of(LocalDate.now(), estStartTime));
        LocalDateTime end = TimeConversions.fromEstToLocal(LocalDateTime.of(LocalDate.now(), estEndTime));

        while (start.isBefore(end.minusSeconds(1))) {
            startTimeCombo.getItems().add(LocalTime.from(start));
            start = start.plusMinutes(15);
        }
        startTimeCombo.setPromptText("Select start time");


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

        Callback<ListView<User>, ListCell<User>> userFactorySelected = lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : (user.toString()));
            }
        };

        userCombo.setCellFactory(userFactory);
        userCombo.setButtonCell(userFactorySelected.call(null));
        userCombo.getSelectionModel().select(0);
    }

    /**
     * Creates and sets a list for the endTimeCombo of possible end times (between 15 after start time and 22:00 EST)
     *
     * @param event for limiting end times to viable times
     */
    public void endTimeHandler(ActionEvent event) {
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

    // TODO: add logic for scheduling overlapping appointments for customers
    // TODO: add logic for scheduling outside 8am-10pm EST or weekends
    public void confirmHandler(ActionEvent event) {

        if (datePicker.getValue() == null) {
            System.out.println("datepicker null");
            return;
        }

        LocalDate date = datePicker.getValue();

        LocalTime estStartTime = LocalTime.of(8, 0);
        LocalTime estEndTime = LocalTime.of(22, 0);

        LocalDateTime estStart = LocalDateTime.of(date, estStartTime);
        LocalDateTime estEnd = LocalDateTime.of(date, estEndTime);

        LocalTime startTimer = startTimeCombo.getValue();
        LocalDateTime startLdt = LocalDateTime.of(date, startTimer);
        LocalDateTime starter = Bundles.TimeConversions.convertToEst(startLdt);

        LocalTime endTimer = endTimeCombo.getValue();
        LocalDateTime endLdt = LocalDateTime.of(date, endTimer);
        LocalDateTime ender = Bundles.TimeConversions.convertToEst(endLdt);


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
            } else if (datePicker.getValue().getDayOfWeek() == DayOfWeek.SATURDAY ||
                    datePicker.getValue().getDayOfWeek() == DayOfWeek.SUNDAY
            ) {
                feedbackLabel.setText("Error: Cannot schedule appointment on weekend");
                feedbackLabel.setTextFill(Color.color(0.6, 0.2, 0.2));
            } else if (starter.isBefore(estStart) ||
                    starter.isAfter(estEnd) ||
                    ender.isBefore(starter) ||
                    ender.isAfter(estEnd)
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

                DBAppointments.addAppointment(title, description, location, type, start, end, userCombo.getValue().toString(), customerId, userId, contactId);

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

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainViewScene);
            window.show();
        }
    }

}
