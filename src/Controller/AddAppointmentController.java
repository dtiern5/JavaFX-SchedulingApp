package Controller;

import DBAccess.DBContacts;
import DBAccess.DBCountries;
import DBAccess.DBCustomers;
import DBAccess.DBDivisions;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
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
