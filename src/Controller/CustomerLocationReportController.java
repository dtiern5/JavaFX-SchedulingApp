package Controller;

import DBAccess.DBCountries;
import DBAccess.DBReports;
import Model.Country;
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
 * Controller for the CustomerLocationReport screen where the number of customers per division is displayed.
 */
public class CustomerLocationReportController implements Initializable {

    @FXML
    private TableView<Report> reportTableView;
    @FXML
    private TableColumn divisionColumn;
    @FXML
    private TableColumn countColumn;

    @FXML
    private ComboBox<Country> countryComboBox;

    /**
     * Sets the ComboBox for selecting a country.
     *
     * @param url the location used to resolve relative paths for the root object
     * @param resourceBundle resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize countryComboBox
        try {
            ObservableList<Country> countryList = DBCountries.getAllACountries();
            countryComboBox.setItems(countryList);
            countryComboBox.setPromptText("Select Country");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Searches the database for a selected country's divisions.
     * Displays a customer count for each division with at least one customer.
     *
     * @param event for displaying appointments of a contact on click
     * @throws SQLException signals a SQL Exception has occurred
     */
    public void searchHandler(ActionEvent event) throws SQLException {
        if (countryComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Must select country");
            alert.showAndWait();
        } else {
            ObservableList<Report> divisionList = null;
            divisionList = DBReports.countDivisions(countryComboBox.getValue());
            divisionColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
            reportTableView.setItems(divisionList);
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
