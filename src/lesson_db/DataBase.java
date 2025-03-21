package lesson_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class DataBase {

    private Connection connection;

    public DataBase() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }

    // Проверка соединения
    public boolean isConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Ошибка проверки соединения: " + e.getMessage());
            return false;
        }
    }

    // Добавление новой брони места
    public void addBooking(int seatId, String guestName) {
        String sql = "INSERT INTO bookings (seat_id, guest_name, booking_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, seatId);
            statement.setString(2, guestName);
            statement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            statement.executeUpdate();

            // Обновляем статус места на "забронирован"
            updateSeatStatus(seatId, true);
            System.out.println("Бронирование успешно добавлено.");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении бронирования: " + e.getMessage());
        }
    }

    // Вывод всех бронирований
    public void getAllBookings() {
        String sql = "SELECT b.id, s.seat_number, b.guest_name, b.booking_date FROM bookings b JOIN seats s ON b.seat_id = s.id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Список всех бронирований:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int seatNumber = resultSet.getInt("seat_number");
                String guestName = resultSet.getString("guest_name");
                String bookingDate = resultSet.getDate("booking_date").toString();
                System.out.printf("ID: %d, Номер места: %d, Имя гостя: %s, Дата брони: %s%n",
                        id, seatNumber, guestName, bookingDate);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении бронирований: " + e.getMessage());
        }
    }

    // Вывод всех свободных мест
    public void getAvailableSeats() {
        String sql = "SELECT * FROM seats WHERE is_booked = FALSE";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Список свободных мест:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int seatNumber = resultSet.getInt("seat_number");
                System.out.printf("ID: %d, Номер места: %d%n", id, seatNumber);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении свободных мест: " + e.getMessage());
        }
    }

    // Обновление статуса места
    private void updateSeatStatus(int seatId, boolean isBooked) {
        String sql = "UPDATE seats SET is_booked = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isBooked);
            statement.setInt(2, seatId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении статуса места: " + e.getMessage());
        }
    }

    // Закрытие соединения
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Соединение закрыто.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }
}