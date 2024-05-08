package lk.ijse.dep12.jdbc.sql_injection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.dep12.jdbc.sql_injection.db.SingletonConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Connection connection = SingletonConnection.getInstance().getConnection(); //get connection from singleton connection class
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down");
            try {
                //if connection is not closed when app is shut down, close the connection
                if (connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));

        primaryStage.setScene(new Scene(FXMLLoader
                .load(getClass().getResource("/view/LoginUserNameView.fxml"))));
        primaryStage.setTitle("SQL Injection: Login");
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.centerOnScreen();

    }
}
