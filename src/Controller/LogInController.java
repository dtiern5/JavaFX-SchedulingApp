package Controller;

import DBAccess.DBCountries;
import Database.DBConnection;
import Model.Countries;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import javax.swing.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialized");
    }


    // Code from Getting The DBConnection Class Project Ready
    public void OnButtonAction(ActionEvent actionEvent) {
        ObservableList<Countries> countryList = DBCountries.getAllCountries();
        for (Countries C : countryList) {
            System.out.println("Country ID : " + C.getCountryID() + "   Name : " + C.getCountryName());
        }

    }

    public void exitButtonPushed() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            DBConnection.closeConnection();
            System.exit(0);
        }
    }

    public void loginButtonPushed() {
        
    }

}
