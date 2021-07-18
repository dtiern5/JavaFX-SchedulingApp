package Controller;

import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import Model.Appointment;
import Model.Contact;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the ContactSchedReport screen where appointments are displayed for individual contacts.
 */
public class ContactSchedReportController implements Initializable {

    @FXML
    private TableView<Appointment> appointmentsTableView;
    @FXML
    private TableColumn<Appointment, Integer> appointmentsIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsTitleColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsTypeColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsDescriptionColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsStartTimeColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentsEndTimeColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentsCustomerIdColumn;

    @FXML
    private ComboBox<Contact> contactCombo;

    private ObservableList<Appointment> appointmentList;

    /**
     * Sets the ComboBox for selecting a contact.
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

        try {
            appointmentList = DBAppointments.getAllAppointments();

            appointmentsIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            appointmentsTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            appointmentsDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
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
     * Searches the database for a selected contact's associated appointments.
     *
     */
    public void searchHandler() {
        if (contactCombo.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Must select contact");
            alert.showAndWait();
        } else {
            populateAppointmentTable();
        }
    }

    /**
     * Displays appointments of the selected contact in the TableView.
     *
     * Using a lambda increases efficiency here. We create a list of all appointments by accessing the database only
     * once. The lambda expression simply filters that list based on the selected Contact.
     *
     */
    private void populateAppointmentTable() {
        Contact contact = contactCombo.getValue();

        FilteredList<Appointment> contactAppointments = new FilteredList<>(appointmentList);

        // check if the contact for the appointment equals the selected contact
        contactAppointments.setPredicate(a -> a.getContact().toString().equals(contact.toString()));
        appointmentsTableView.setItems(contactAppointments);
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
