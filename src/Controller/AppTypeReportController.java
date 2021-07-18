package Controller;

import DBAccess.DBReports;
import Model.Appointment;
import Model.Report;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Year;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the AppTypeReport screen where appointments are counted by type and month.
 */
public class AppTypeReportController implements Initializable {

    @FXML
    private ComboBox<String> monthCombo;
    @FXML
    private ComboBox<Year> yearCombo;
    @FXML
    private TableView<Report> reportTableView;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, Integer> countColumn;


    /**
     * Sets the ComboBoxes for month and year selection.
     *
     * @param url the location used to resolve relative paths for the root object
     * @param resourceBundle resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthCombo.getItems().addAll("1. January", "2. February", "3. March",
                "4. April", "5. May", "6. June", "7. July", "8. August", "9. September",
                "10. October", "11. November", "12. December");

                // Populate year ComboBox (arbitrarily 5 years back and 5 years out)
        Year startYear = Year.now().minusYears(5);
        Year endYear = Year.now().plusYears(6);

        ObservableList<Year> yearObservableList = FXCollections.observableArrayList();
        while (startYear.isBefore(endYear)) {
            yearObservableList.add(startYear);
            startYear = startYear.plusYears(1);
        }

        yearCombo.setItems(yearObservableList);
    }

    /**
     * Searches the database for a count of appointment types in the selected month and year.
     *
     * @throws SQLException signals a SQL Exception has occurred
     */
    public void searchHandler() throws SQLException {
        if (yearCombo.getValue() == null || monthCombo.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Must select month and year");
            alert.showAndWait();
        } else {
            // Convert year to integer
            String yearString = String.valueOf((yearCombo.getValue()));
            int yearInt = Integer.parseInt(yearString);

            // Convert month to integer, removing all non-digit characters
            String monthString = monthCombo.getValue();
            String monthDigitString = monthString.replaceAll("[^0-9]", "");
            int monthInt = Integer.parseInt(monthDigitString);

            ObservableList<Report> appointmentList;
            appointmentList = DBReports.countAppointmentTypes(yearInt, monthInt);
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
            reportTableView.setItems(appointmentList);
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

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainViewScene);
            window.show();
        }
    }
}
