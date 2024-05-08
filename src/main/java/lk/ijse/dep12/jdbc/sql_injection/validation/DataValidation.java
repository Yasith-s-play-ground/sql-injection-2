package lk.ijse.dep12.jdbc.sql_injection.validation;

public class DataValidation {
    public static boolean isValidUserName(String username) {
        return username.strip().matches("[a-zA-Z0-9_-]{3,15}");
    }

    public static boolean isValidPassword(String password) {
        return password.strip().length() > 3 && password.matches(".*\\d.*")
               && password.matches(".*[A-Za-z].*");
    }

    public static boolean isValidName(String name) {
        return name.strip().matches("[a-zA-Z ]{3,}");
    }

}
