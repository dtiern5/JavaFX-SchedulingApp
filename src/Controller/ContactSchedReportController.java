package Controller;

import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import DBAccess.DBReports;
import Model.Appointment;
import Model.Contact;
import Model.Report;
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

    /**
     * Sets the ComboBox for selecting a contact.
     *
     * @param url the location used to resolve relative paths for the root object
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
    }

    /**
     * Searches the database for a selected contact's associated appointments.
     *
     * @param event for displaying appointments of a contact on click
     * @throws SQLException signals a SQL Exception has occurred
     */
    public void searchHandler(ActionEvent event) throws SQLException {
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
     * @throws SQLException signals a SQL Exception has occurred
     */
    private void populateAppointmentTable() throws SQLException {
        ObservableList<Appointment> appointmentList = null;
        Contact contact = contactCombo.getValue();

        appointmentList = DBAppointments.getAppointmentsByContact(contact);
        appointmentsIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentsTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentsDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentsStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        appointmentsEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        appointmentsCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentsTableView.setItems(appointmentList);
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
