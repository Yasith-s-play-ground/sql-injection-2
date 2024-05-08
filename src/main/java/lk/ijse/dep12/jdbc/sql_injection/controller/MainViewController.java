package lk.ijse.dep12.jdbc.sql_injection.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainViewController {

    public Button btnLogout;

    public Label lblDateTime;

    public Label lblUserName;

    public void initialize() {
        Task<String> dateTimeTask = new Task<>() {

            @Override
            protected String call() throws Exception {
                while (true) {
                    updateValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    Thread.sleep(1000);
                }
            }
        };

        lblDateTime.textProperty().bind(dateTimeTask.valueProperty());
        new Thread(dateTimeTask).start();

        //get full name of current logged user
        String fullName = System.getProperty("app.principal.fullName"); // authorised user is known as principal
        lblUserName.setText(fullName);
    }

    public void btnLogoutOnAction(ActionEvent event) throws IOException {
        System.getProperties().remove("app.principal.username"); // remove principal username when logging out
        System.getProperties().remove("app.principal.fullName"); // remove principal full name when logging out
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginUserNameView.fxml"))));
        stage.setTitle("SQL Injection: Login");
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        ((Stage) btnLogout.getScene().getWindow()).close();
    }

}
