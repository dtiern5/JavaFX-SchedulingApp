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

        String updateStatement = "UPDATE countries SET Country = ?, Created_By = ? WHERE Country = ?";

        DBQuery.setPreparedStatement(conn, updateStatement); // Create preparedStatement

        PreparedStatement ps = DBQuery.getPreparedStatement();

        String countryName, newCountry, createdBy;

        // Get keyboard input
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter country to update: ");
        countryName = scanner.nextLine();

        System.out.print("Enter new country: ");
        newCountry = scanner.nextLine();

        System.out.print("Enter user: ");
        createdBy = scanner.nextLine();

        ps.setString(1, newCountry);
        ps.setString(2, createdBy);
        ps.setString(3, countryName);

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
