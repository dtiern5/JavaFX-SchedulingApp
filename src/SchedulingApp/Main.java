package SchedulingApp;

import Database.DBConnection;
import Database.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/LogInView.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        Connection conn = DBConnection.startConnection();

        DBQuery.setStatement(conn); // Create Statement object
        Statement statement = DBQuery.getStatement(); // Get Statement reference

        // Raw SQL insert statement
        //String insertStatement = "INSERT INTO countries(Country, Created_By, Last_Updated_By) " +
        //        "VALUES ('US', 'admin', 'admin')";

        /*
        // Variable Insert
        String countryName = "Canada";
        String Create_Date = "2021-06-28 00:00:00";
        String Created_By = "admin";
        String Last_Updated_By = "admin";

        String insertStatement = "INSERT INTO countries(Country, Create_Date, Created_By, Last_Updated_By) " +
                "VALUES (" +
                "'" + countryName + "', " +
                "'" + Create_Date + "', " +
                "'" + Created_By + "', " +
                "'" + Last_Updated_By + "'" +
                ")";
         */

        // Update statement
        // String updateStatement = "UPDATE countries SET Country = 'Japan' WHERE Country_ID = 6";

        // Delete statement
        String deleteStatement = "DELETE FROM countries WHERE Country = 'Japan'";


                // Execute SQL statement
        statement.execute(deleteStatement);

        // Confirm rows affected
        if(statement.getUpdateCount() > 0)
            System.out.println(statement.getUpdateCount() + " row(s) affected");
        else
            System.out.println("No change");

        launch(args);

        DBConnection.closeConnection();
    }
}
