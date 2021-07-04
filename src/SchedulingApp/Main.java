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

        String insertStatement = "INSERT INTO countries(Country, Create_Date, Created_By, Last_Updated_By) VALUES(?, ?, ?, ?)";

        DBQuery.setPreparedStatement(conn, insertStatement); // Create preparedStatement

        PreparedStatement ps = DBQuery.getPreparedStatement();

        String countryName;
        String createDate = "2021-06-02";
        String createdBy = "admin";
        String lastUpdatedBy = "admin";

        // Get keyboard input
        Scanner scanner = new Scanner(System.in);
        countryName = scanner.nextLine();

        // key-value mapping
        ps.setString(1, countryName);
        ps.setString(2, createDate);
        ps.setString(3, createdBy);
        ps.setString(4, lastUpdatedBy);

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
