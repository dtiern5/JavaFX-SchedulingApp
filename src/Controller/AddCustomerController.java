package Controller;

import DBAccess.DBDivisions;
import Database.DBConnection;
import Database.DBQuery;
import Model.Division;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    @FXML
    private TextField customerIdTF;
    @FXML
    private TextField nameTF;
    @FXML
    private TextField addressTF;
    @FXML
    private TextField postalCodeTF;
    @FXML
    private ComboBox<Division> divisionComboBox;
    @FXML
    private TextField countryTF;
    @FXML
    private TextField phoneNumberTF;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Division> divisionList = DBDivisions.getAllDivisions();
            divisionComboBox.setItems(divisionList);
            divisionComboBox.setPromptText("First Level Division");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Reverts back to main screen
     *
     * @param event for changing scene on click
     * @throws IOException signals I/O exception has occurred
     */
    public void goBackHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to go back?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            Parent MainScreenViewParent = FXMLLoader.load(getClass().getResource("../View/MainScreenView.fxml"));
            Scene mainViewScene = new Scene(MainScreenViewParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainViewScene);
            window.show();
        }
    }

    public void confirmHandler(ActionEvent event) {
        Connection conn = DBConnection.getConnection();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        try{
            alert.setTitle("Customer not added");

            if(nameTF.getText().isEmpty() ||
            addressTF.getText().isEmpty() ||
            postalCodeTF.getText().isEmpty() ||
            phoneNumberTF.getText().isEmpty()) {
                alert.setContentText("All fields require values");
                alert.showAndWait();
            }

            String insertStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone)" +
                    "VALUES(?, ?, ?, ?)";

            DBQuery.setPreparedStatement(conn, insertStatement);

            PreparedStatement ps = DBQuery.getPreparedStatement();

            String customerName = nameTF.getText();
            String address = addressTF.getText();
            String postalCode = postalCodeTF.getText();
            String phoneNumber = phoneNumberTF.getText();

            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);

            ps.execute();

        } catch (Exception e) {
            System.out.println("Input error: " + e.getMessage());
            alert.setTitle("Input error");
            alert.setContentText("Placeholder");
        }
    }
}
