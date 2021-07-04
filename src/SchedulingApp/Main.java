package SchedulingApp;

import DBAccess.DBCountries;
import Database.DBConnection;
import Database.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/LogInView.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        Connection conn = DBConnection.startConnection(); // Connect to database
        launch(args);
        DBConnection.closeConnection();
    }
}
