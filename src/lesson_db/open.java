package lesson_db;

public class open {

    public static void main(String[] args) {
        DataBase db = new DataBase();

        if (db.isConnection()) {
            System.out.println("Соединение установлено.");

            // Добавление 10 бронирований
            for (int i = 1; i <= 10; i++) {
                db.addBooking(i, "Гость " + i);
            }

            // Вывод всех бронирований
            db.getAllBookings();

            // Вывод доступных номеров
            db.getAvailableRooms();

            // Закрытие соединения
            db.closeConnection();
        } else {
            System.out.println("Не удалось установить соединение.");
        }
    }
}