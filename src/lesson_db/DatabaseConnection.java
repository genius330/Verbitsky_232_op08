package lesson_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/cinema"; // URL базы данных
    private static final String USER = "postgres"; // Имя пользователя
    private static final String PASSWORD = " "; // Пароль

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}