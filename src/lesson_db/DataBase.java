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

    // Добавление новой брони
    public void addBooking(int roomId, String guestName) {
        String sql = "INSERT INTO bookings (room_id, guest_name, booking_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            statement.setString(2, guestName);
            statement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            statement.executeUpdate();

            // Обновляем статус номера на "забронирован"
            updateRoomStatus(roomId, true);
            System.out.println("Бронирование успешно добавлено.");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении бронирования: " + e.getMessage());
        }
    }
    // Вывод всех бронирований
    public void getAllBookings() {
        String sql = "SELECT b.id, r.room_number, b.guest_name, b.booking_date FROM bookings b JOIN rooms r ON b.room_id = r.id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Список всех бронирований:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int roomNumber = resultSet.getInt("room_number");
                String guestName = resultSet.getString("guest_name");
                String bookingDate = resultSet.getDate("booking_date").toString();
                System.out.printf("ID: %d, Номер комнаты: %d, Имя гостя: %s, Дата брони: %s%n",
                        id, roomNumber, guestName, bookingDate);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении бронирований: " + e.getMessage());
        }
    }

    // Вывод всех доступных номеров
    public void getAvailableRooms() {
        String sql = "SELECT * FROM rooms WHERE is_booked = FALSE";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Список доступных номеров:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int roomNumber = resultSet.getInt("room_number");
                System.out.printf("ID: %d, Номер комнаты: %d%n", id, roomNumber);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении доступных номеров: " + e.getMessage());
        }
    }

    // Обновление статуса номера
    private void updateRoomStatus(int roomId, boolean isBooked) {
        String sql = "UPDATE rooms SET is_booked = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isBooked);
            statement.setInt(2, roomId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении статуса номера: " + e.getMessage());
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