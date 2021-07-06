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
import javafx.scene.control.*;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    private Label appName;
    @FXML
    private Button loginButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;
    private String exitPrompt;
    private String closeAlertTitle;
    private String userNameNotFound;
    private String incorrectPassword;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("Bundles.MessagesBundle", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                System.out.println(rb.getString("hello") + " " + rb.getString("world"));
            }
            exitButton.setText(rb.getString("Exit"));
            loginButton.setText(rb.getString("Login"));
            userNameLabel.setText(rb.getString("Username"));
            passwordLabel.setText(rb.getString("Password"));
            appName.setText(rb.getString("App_Name"));
            exitPrompt = rb.getString("Exit_prompt");
            closeAlertTitle = rb.getString("Close_alert_title");
            userNameNotFound = rb.getString("Username_not_found");
            incorrectPassword = rb.getString("Incorrect_password");
        } catch (Exception e) {
            System.out.println("Language not supported");
            System.out.println("Exiting");
            System.exit(0);
        }
        System.out.println("Initialized");
    }

    public void exitButtonPushed() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, exitPrompt);
        alert.setTitle(closeAlertTitle);
        alert.setHeaderText("");
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(userNameNotFound);
            alert.showAndWait();
        } else {
            if (passwordAttempt.equals(DBUsers.getPassword(userNameAttempt))) {
                System.out.println("Logged in!");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(incorrectPassword);
                alert.showAndWait();
            }
        }

    }

}

