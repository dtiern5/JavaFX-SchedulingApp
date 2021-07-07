package Controller;

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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

    public User currentUser;

    @FXML
    private ComboBox<LocalTime> combo3;

    @FXML
    private Label userLabel;


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
    public void cancelHandler(ActionEvent event) throws IOException {
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
