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

        String selectStatement = "SELECT * FROM countries";

        DBQuery.setPreparedStatement(conn, selectStatement); // Create preparedStatement

        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.execute();

        ResultSet rs = ps.getResultSet();

        // Forward scroll ResultSet
        while(rs.next()) {
            int countryId = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            LocalDate date = rs.getDate("Create_Date").toLocalDate();
            LocalTime time = rs.getTime("Create_Date").toLocalTime();
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();

            // Display record
            System.out.println(countryId + " | " +
                    countryName + " | " +
                    date + " " + time + " | " +
                    createdBy + " | " +
                    lastUpdate);
        }

        launch(args);
        DBConnection.closeConnection();
    }
}
