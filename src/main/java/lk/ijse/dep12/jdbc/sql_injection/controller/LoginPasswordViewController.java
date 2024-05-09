package lk.ijse.dep12.jdbc.sql_injection.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import lk.ijse.dep12.jdbc.sql_injection.db.SingletonConnection;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPasswordViewController {

    public Button btnBack;
    public Button btnExit;
    public Button btnLogin;
    public PasswordField txtPassword;
    public Label lblFullName;

    public void initialize() {
        lblFullName.setText(System.getProperty("app.principal.fullName")); // set full name to label
    }

    public void btnBackOnAction(ActionEvent event) throws IOException {
        removeAddedSystemProperties();
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginUserNameView.fxml"))));
        //stage.setOnCloseRequest(Event::consume);
        stage.setTitle("SQL Injection: Login");
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        ((Stage) (btnBack.getScene().getWindow())).close();
    }

    public void btnExitOnAction(ActionEvent event) {
        Platform.exit();
    }

    public void btnLoginOnAction(ActionEvent event) {
        String password = txtPassword.getText();
        //password validation
        if (password.isBlank() || password.strip().length() < 4 || !password.matches(".*\\d.*")
            || !password.matches(".*[A-Za-z].*")) {
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return;
        }

        try {
            String userName = System.getProperty("app.principal.username");
            Connection connection = SingletonConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    SELECT * FROM "user" WHERE username=?
                    """);
            preparedStatement.setString(1, userName.strip());
            ResultSet rst = preparedStatement.executeQuery();
            rst.next();
            String hashedPassword = rst.getString("password"); //get password from database
            if (hashedPassword.equals(DigestUtils.sha256Hex(password))) { //compare hash of entered password
//                String fullName = rst.getString("full_name"); //get user's full name
//                System.setProperty("app.principal.fullName", fullName); // set full name property
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainView.fxml"))));
                stage.setOnCloseRequest(Event::consume);
                stage.setTitle("SQL Injection: Main");
                stage.setResizable(false);
                stage.show();
                stage.centerOnScreen();
                ((Stage) (btnLogin.getScene().getWindow())).close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid password, try again!!").show();
                txtPassword.requestFocus();
                txtPassword.selectAll();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong, please try again").show();
        }
    }

    private void removeAddedSystemProperties() {
        System.getProperties().remove("app.principal.username"); // remove principal username when logging out
        System.getProperties().remove("app.principal.fullName"); // remove principal full name when logging out
    }

}
