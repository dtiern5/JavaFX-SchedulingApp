package Controller;


import DBAccess.DBAppointments;
import DBAccess.DBUsers;
import Database.DBConnection;
import Model.Appointment;
import Model.User;
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

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the log in screen.
 */
public class LogInController implements Initializable {

    public User currentUser;
    private String exitPrompt;
    private String closeAlertTitle;
    private String userNameNotFound;
    private String incorrectPassword;
    private String appointmentAlert;
    private String minutes;
    private String noAppointments;
    private String zoneId;

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
    @FXML
    private Label locationLabel;


    /**
     * Checks if current system language is French or English.
     * If so, sets all the text in the appropriate language.
     * If not, prints an error to the system and exits.
     *
     * @param url the location used to resolve relative paths for the root object
     * @param resourceBundle resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("BundlesAndUtilities.MessagesBundle", Locale.getDefault());
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
            appointmentAlert = rb.getString("Appointment_Alert");
            minutes = rb.getString("minutes");
            noAppointments = rb.getString("No_Appointment_Alert");
            zoneId = rb.getString("Zone_ID");
        } catch (Exception e) {
            System.out.println("Language not supported");
            System.out.println("Exiting");
            System.exit(0);
        }
        locationLabel.setText(zoneId + ": " + ZoneId.systemDefault());
        System.out.println("Initialized");
    }

    /**
     * Confirms user wants to exit the program.
     */
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
     * Checks given username and password against the database.
     * Records the log in attempt in a local text file.
     * If values are valid, opens the main screen of the program.
     *
     * @param event for logging in to the program on click
     * @throws Exception signals an exception has occurred with the FileWriter or database access
     */
    public void handleLogin(ActionEvent event) throws Exception {

        String userNameAttempt = userNameTextField.getText();
        String passwordAttempt = passwordTextField.getText();

        FileWriter fileWriter = new FileWriter("login_activity.txt", true);

        fileWriter.append("Log in attempt at " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + " on " + LocalDate.now() + "\n");
        // fileWriter.append("Log in attempt at " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) + "\n");


        /*
         * First check if user name is in the database
         * If so, check if the associated password is correct
         */
        if (!DBUsers.findUserName(userNameAttempt)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(userNameNotFound);
            alert.showAndWait();
            fileWriter.append("   Unsuccessful (Username not found)\n\n");
            fileWriter.close();
        } else {
            if (passwordAttempt.equals(DBUsers.getPassword(userNameAttempt))) {
                fileWriter.append("   User '" + userNameAttempt + "' logged in successfully\n\n");
                fileWriter.close();
                currentUser = DBUsers.getUserByName(userNameAttempt);

                appointmentAlert();
                openMainScreen(event);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(incorrectPassword);
                alert.showAndWait();
                fileWriter.append("   Unsuccessful (wrong password for user '" + userNameAttempt + "')\n\n");
                fileWriter.close();
            }
        }

    }

    /**
     * Checks if the logged in user has an appointment within the next 15 minutes
     * @throws SQLException signals a SQL Exception has occurred
     */
    private void appointmentAlert() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("User: " + userNameTextField.getText());

        ObservableList<Appointment> todaysAppointments = DBAppointments.getTodaysAppointmentsByUser(userNameTextField.getText());

        LocalTime currentTime = LocalTime.now();

        boolean upcomingMeeting = false;
        Long timeToMeeting = null;

        for (Appointment a : todaysAppointments) {
            LocalTime startTime = a.getStartTime().toLocalTime();

            long timeDifference = ChronoUnit.MINUTES.between(currentTime, startTime);

            System.out.println(timeDifference);

            if (timeDifference <= 15 && timeDifference >= 0) {
                upcomingMeeting = true;
                timeToMeeting = timeDifference;
            }
        }

        if (upcomingMeeting) {
            alert.setContentText(appointmentAlert + " " + timeToMeeting + " " + minutes);
        } else {
            alert.setContentText(noAppointments);
        }
        alert.showAndWait();
    }

    /**
     * Switches to Main scene.
     *
     * @param event for changing scene on click
     * @throws IOException signals I/O exception has occurred
     */
    public void openMainScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("../View/MainScreenView.fxml"));
        Parent scene = loader.load();
        Scene mainViewScene = new Scene(scene);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(mainViewScene);
        window.show();

    }
}

