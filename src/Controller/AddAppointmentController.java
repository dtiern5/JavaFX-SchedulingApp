package Controller;

import Model.Contact;
import Model.Customer;
import Model.User;
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
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

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
    private ComboBox<Contact> contactComboBox;
    @FXML
    private ComboBox<Customer> customerIdCombo;
    @FXML
    private ComboBox<Integer> yearCombo;
    @FXML
    private ComboBox<Integer> monthCombo;
    @FXML
    private ComboBox<Integer> dayCombo;
    @FXML
    private ComboBox<LocalTime> startTimeCombo;
    @FXML
    private ComboBox<LocalTime> endTimeCombo;

    @FXML
    private ComboBox<LocalTime> combo3;


    public void initData(User user) {
        currentUser = user;
        userLabel.setText("Current user: " + currentUser);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Code from Combo Boxes webinar
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);

        while (start.isBefore(end.plusSeconds(1))) {
            combo3.getItems().add(start);
            start = start.plusMinutes(15);
        }
        combo3.getSelectionModel().select(LocalTime.of(8, 0));
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
