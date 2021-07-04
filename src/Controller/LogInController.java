package Controller;

import DBAccess.DBCountries;
import DBAccess.DBUsers;
import Database.DBConnection;
import Database.DBQuery;
import Model.Countries;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Optional;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;

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

    /**
     *
     * @param event
     * @throws Exception
     */
    public void loginButtonPushed(ActionEvent event) throws Exception {

        String userNameAttempt = userNameTextField.getText();
        String passwordAttempt = passwordTextField.getText();

        /*
        * First check if user name is in the database
        * If so, check if the associated password is correct
        */
        if (!DBUsers.findUserName(userNameAttempt)) {
            Alert userNameAlert = new Alert(Alert.AlertType.ERROR);
            userNameAlert.setTitle("Incorrect user name");
            userNameAlert.setContentText("User name not found in database");
            userNameAlert.showAndWait();
        } else {
            if (passwordAttempt.equals(DBUsers.getPassword(userNameAttempt))) {
                System.out.println("Logged in!");
            } else {
                Alert passwordAlert = new Alert(Alert.AlertType.ERROR);
                passwordAlert.setTitle("Information not found");
                passwordAlert.setHeaderText("Incorrect password");
                passwordAlert.showAndWait();
            }
        }

    }

}

