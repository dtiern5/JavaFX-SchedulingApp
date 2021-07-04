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

        String deleteStatement = "DELETE FROM countries WHERE Country = ?";

        DBQuery.setPreparedStatement(conn, deleteStatement); // Create preparedStatement

        PreparedStatement ps = DBQuery.getPreparedStatement();

        // Get keyboard input
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter country to delete: ");
        String countryName = scanner.nextLine();

        ps.setString(1, countryName);

        ps.execute(); // Execute PreparedStatement

        // Check rows affected
        if(ps.getUpdateCount() > 0) {
            System.out.println(ps.getUpdateCount() + " row(s) affected");
        } else {
            System.out.println("No change");
        }

        launch(args);
        DBConnection.closeConnection();
    }
}
