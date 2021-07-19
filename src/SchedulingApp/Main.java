package SchedulingApp;

import Database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

/**
 * <p>
 *     Main class of the application
 * </p>
 * <p>
 *     JavaDoc located in top Directory within folder named 'JavaDoc'
 * </p>
 */
public class Main extends Application {

    /**
     * Starts the JavaFX GUI.
     * @param primaryStage the stage for the main screen
     * @throws Exception signals an exception has occurred
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../View/LogInView.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    /**
     * The application's entry point. Provides connection to the database, then launches GUI.
     * Closes the connection on exit.
     *
     * @param args an array of command-line arguments for the application
     */
    public static void main(String[] args) throws SQLException {
        Connection conn = DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
