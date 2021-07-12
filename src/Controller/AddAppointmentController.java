package Controller;

import DBAccess.*;
import Database.DBConnection;
import Model.*;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Controller for the AddAppointment screen
 */
public class AddAppointmentController<value> implements Initializable {

    public User currentUser;

    @FXML
    private Label userLabel;

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

    private LocalTime startTime;


    /**
     * Accepts and displays the current user.
     *
     * @param user logged in user
     */
    public void initData(User user) {
        currentUser = user;
        userLabel.setText("Current user: " + currentUser);
    }

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

        customerIdCombo.setItems(customerList);
        customerIdCombo.setPromptText("Select Customer");


        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);

        while (start.isBefore(end.minusSeconds(1))) {
            startTimeCombo.getItems().add(start);
            start = start.plusMinutes(15);
        }

        startTimeCombo.setPromptText("Select start time");
    }

    /**
     * Creates and sets a list for the endTimeCombo of possible end times (between 15 after start time and 22:00 EST)
     *
     * @param event for limiting end times to viable times
     */
    public void endTimeHandler(ActionEvent event) {
        endTimeCombo.getItems().clear();

        LocalTime availableEndTime = startTimeCombo.getValue().plusMinutes(15);
        LocalTime lastAvailableTime = LocalTime.of(22, 0);

        while (availableEndTime.isBefore(lastAvailableTime.plusSeconds(1))) {
            endTimeCombo.getItems().add(availableEndTime);
            availableEndTime = availableEndTime.plusMinutes(15);
            endTimeCombo.getSelectionModel().select(0);
        }

    }


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
                System.out.println(title);
                String description = descriptionTF.getText();
                System.out.println(description);
                String location = locationTF.getText();
                String type = typeTF.getText();
                System.out.println("test spot");

                LocalDate chosenDate = datePicker.getValue();

                System.out.println(chosenDate);
                LocalTime startTime = startTimeCombo.getValue();
                System.out.println(startTime);
                LocalTime endTime = endTimeCombo.getValue();
                System.out.println("Chosen Date: " + chosenDate);
                System.out.println("Start time: " + startTime);
                LocalDateTime start = LocalDateTime.of(chosenDate, startTime);
                LocalDateTime end = LocalDateTime.of(chosenDate, endTime);

                System.out.println("Start: " + start);
                System.out.println("End: " + end);

                int customerId = customerIdCombo.getValue().getCustomerId();
                int userId = currentUser.getUserId();
                int contactId = contactCombo.getValue().getContactId();

                DBAppointments.addAppointment(title, description, location, type, start, end, currentUser.toString(), customerId, userId, contactId);

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
