package lk.ijse.dep12.jdbc.sql_injection.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.dep12.jdbc.sql_injection.db.SingletonConnection;
import lk.ijse.dep12.jdbc.sql_injection.validation.DataValidation;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterNewUserViewController {

    public Button btnCreate;

    public Button btnExit;

    public TextField txtFullName;

    public PasswordField txtPassword;

    public PasswordField txtRepeatPassword;

    public TextField txtUserName;
    public Button btnBack;

    public void btnCreateOnAction(ActionEvent event) {
        if (!dataValidation()) return;

        try {
            Connection connection = SingletonConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    INSERT INTO "user" (username, full_name, password) VALUES (?,?,?)
                    """);
            preparedStatement.setString(1, txtUserName.getText().strip());
            preparedStatement.setString(2, txtFullName.getText().strip());
            preparedStatement.setString(3, DigestUtils.sha256Hex(txtPassword.getText().strip()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 1) {
                new Alert(Alert.AlertType.CONFIRMATION, "User added successfully!!").show();
                clearForm();
            } else {
                new Alert(Alert.AlertType.ERROR, "User adding failed!!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearForm() {
        for (TextField textField : new TextField[]{txtFullName, txtUserName, txtPassword, txtRepeatPassword}) {
            textField.clear();
        }
    }

    private boolean dataValidation() {
        if (!DataValidation.isValidUserName(txtUserName.getText())) {
            txtUserName.requestFocus();
            txtUserName.selectAll();
            return false;
        }

        if (!DataValidation.isValidName(txtFullName.getText())) {
            txtFullName.requestFocus();
            txtFullName.selectAll();
            return false;
        }

        if (!DataValidation.isValidPassword(txtPassword.getText())) {
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return false;
        }

        if (!txtPassword.getText().equals(txtRepeatPassword.getText())) {
            txtRepeatPassword.requestFocus();
            txtRepeatPassword.selectAll();
            return false;
        }

        return true;
    }

    public void btnExitOnAction(ActionEvent event) {
        Platform.exit();
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginUserNameView.fxml"))));
        //stage.setOnCloseRequest(Event::consume);
        stage.setTitle("SQL Injection: Login");
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        ((Stage) (btnBack.getScene().getWindow())).close();
    }
}
