package lk.ijse.dep12.jdbc.sql_injection.db;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SingletonConnection {
    private static SingletonConnection INSTANCE;
    private Connection CONNECTION;

    private SingletonConnection() {
        try {
            Properties properties = new Properties();

            properties.load(getClass().getResourceAsStream("/application.properties"));

            String url = properties.getProperty("app.url");
            String username = properties.getProperty("app.username");
            String password = properties.getProperty("app.password");

            CONNECTION = DriverManager.getConnection(url, username, password);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e instanceof SQLException ? "Failed to establish database connection, try restarting" :
                    "Failed to load configurations")
                    .showAndWait();
            System.exit(1);
        }


    }

    public static SingletonConnection getInstance() {
        return (INSTANCE == null) ? INSTANCE = new SingletonConnection() : INSTANCE;
    }

    public Connection getConnection() {
        return CONNECTION;
    }
}
