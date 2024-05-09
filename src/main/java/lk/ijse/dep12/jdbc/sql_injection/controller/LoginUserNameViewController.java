package lk.ijse.dep12.jdbc.sql_injection.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.dep12.jdbc.sql_injection.db.SingletonConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUserNameViewController {

    public Button btnContinue;
    public Button btnExit;
    public Button btnRegister;
    public TextField txtUserName;

    public void initialize() {
        txtUserName.requestFocus();
    }

    public void btnContinueOnAction(ActionEvent event) {
        String userName = txtUserName.getText();

        //username validation
        if (userName.isBlank() || userName.strip().contains(" ") || userName.strip().length() < 3) {
            txtUserName.requestFocus();
            txtUserName.selectAll();
            return;
        }

        try {
            Connection connection = SingletonConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    SELECT full_name FROM "user" WHERE username=?
                    """);
            preparedStatement.setString(1, userName.strip());
            ResultSet rst = preparedStatement.executeQuery();
            if (rst.next()) {
                System.setProperty("app.principal.username", userName.strip());
                System.setProperty("app.principal.fullName", rst.getString("full_name")); // set full name property
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginPasswordView.fxml"))));
                //stage.setOnCloseRequest(Event::consume);
                stage.setTitle("SQL Injection: Login");
                stage.setResizable(false);
                stage.show();
                stage.centerOnScreen();
                ((Stage) (btnContinue.getScene().getWindow())).close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid username, try again!!").show();
                txtUserName.requestFocus();
                txtUserName.selectAll();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong, please try again").show();
        }
    }

    public void btnExitOnAction(ActionEvent event) {
        Platform.exit();
    }

    public void btnRegisterOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/RegisterNewUserView.fxml"))));
        stage.setTitle("SQL Injection: Register New User");
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        ((Stage) btnRegister.getScene().getWindow()).close();
    }

}
