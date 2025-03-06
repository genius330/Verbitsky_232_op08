package lesson_db;

import java.sql.SQLException;

public class open {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DataBase db = new DataBase();
        db.isConnection();
    }
}
