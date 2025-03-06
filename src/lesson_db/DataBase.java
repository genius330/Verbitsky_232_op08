package lesson_db;

import java.sql.*;

class DataBase {
    private Connection dbCon;

    private Connection getDBConnection() {
        String host = "localhost";
        String port = "5432";
        String dbName = "idiot"; // Убедитесь, что база данных создана
        String str = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
        try {
            Class.forName("org.postgresql.Driver");
            String login = "postgres";
            String password = "";
            dbCon = DriverManager.getConnection(str, login, password);
            System.out.println("Соединение установлено");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер не найден");
        } catch (SQLException e) {
            System.out.println("Неверный путь (логин и пароль)");
        }
        return dbCon;
    }

    public void isConnection() throws SQLException {
        dbCon = getDBConnection();
        System.out.println(dbCon.isValid(1000));
    }

    public void createTables() {
        String createProductsTable = "CREATE TABLE IF NOT EXISTS Products (id SERIAL PRIMARY KEY, name VARCHAR(50), price DECIMAL)";
        String createCustomersTable = "CREATE TABLE IF NOT EXISTS Customers (id SERIAL PRIMARY KEY, name VARCHAR(50), email VARCHAR(50))";
        String createOrdersTable = "CREATE TABLE IF NOT EXISTS Orders (id SERIAL PRIMARY KEY, customer_id INT, order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (customer_id) REFERENCES Customers(id))";
        String createOrderItemsTable = "CREATE TABLE IF NOT EXISTS OrderItems (id SERIAL PRIMARY KEY, order_id INT, product_id INT, quantity INT, FOREIGN KEY (order_id) REFERENCES Orders(id), FOREIGN KEY (product_id) REFERENCES Products(id))";

        try (Statement statement = getDBConnection().createStatement()) {
            statement.executeUpdate(createProductsTable);
            statement.executeUpdate(createCustomersTable);
            statement.executeUpdate(createOrdersTable);
            statement.executeUpdate(createOrderItemsTable);
            System.out.println("Таблицы созданы");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addProducts() {
        String[] productNames = {
                "Laptop Pro X", "Smartphone Z10", "Wireless Headphones", "Gaming Mouse", "External SSD 1TB",
                "Bluetooth Speaker", "4K Monitor", "Mechanical Keyboard", "USB-C Hub", "Portable Power Bank",
                "Digital Camera", "Noise Cancelling Earbuds", "Tablet Air 8", "Gaming Console X", "Fitness Tracker",
                "Smartwatch Pro", "Printer All-in-One", "External HDD 2TB", "VR Headset", "Studio Microphone",
                "Gaming Chair", "Coffee Maker", "Air Fryer", "Refrigerator", "Washing Machine", "Dishwasher",
                "LED TV 55 Inch", "Soundbar System", "Projector 1080p", "E-Book Reader"
        };

        double[] productPrices = {
                999.99, 699.99, 149.99, 59.99, 119.99,
                79.99, 299.99, 89.99, 39.99, 29.99,
                499.99, 129.99, 349.99, 499.99, 79.99,
                199.99, 149.99, 69.99, 299.99, 179.99,
                199.99, 99.99, 79.99, 599.99, 449.99, 399.99,
                349.99, 149.99, 249.99, 119.99
        };

        try (Statement statement = getDBConnection().createStatement()) {
            for (int i = 0; i < productNames.length; i++) {
                String sql = String.format("INSERT INTO Products (name, price) VALUES ('%s', %.2f)", productNames[i], productPrices[i]);
                statement.executeUpdate(sql);
            }
            System.out.println("Добавлено 30 товаров");
        } catch (SQLException e) {
            System.out.println("Не удалось добавить товары: " + e.getMessage());
        }
    }

    public void addCustomers() {
        try (Statement statement = getDBConnection().createStatement()) {
            for (int i = 1; i <= 30; i++) {
                String sql = String.format("INSERT INTO Customers (name, email) VALUES ('Customer%d', 'customer%d@example.com')", i, i);
                statement.executeUpdate(sql);
            }
            System.out.println("Добавлено 30 клиентов");
        } catch (SQLException e) {
            System.out.println("Не удалось добавить клиентов: " + e.getMessage());
        }
    }

    public void addOrders() {
        try (Statement statement = getDBConnection().createStatement()) {
            for (int i = 1; i <= 30; i++) {
                int customerId = (int) (Math.random() * 30) + 1; // случайный id клиента
                String sql = String.format("INSERT INTO Orders (customer_id) VALUES (%d)", customerId);
                statement.executeUpdate(sql);
            }
            System.out.println("Добавлено 30 заказов");
        } catch (SQLException e) {
            System.out.println("Не удалось добавить заказы: " + e.getMessage());
        }
    }

    public void addOrderItems() {
        try (Statement statement = getDBConnection().createStatement()) {
            for (int i = 1; i <= 30; i++) {
                int orderId = (int) (Math.random() * 30) + 1; // случайный id заказа
                int productId = (int) (Math.random() * 30) + 1; // случайный id продукта
                int quantity = (int) (Math.random() * 5) + 1; // случайное количество от 1 до 5
                String sql = String.format("INSERT INTO OrderItems (order_id, product_id, quantity) VALUES (%d, %d, %d)", orderId, productId, quantity);
                statement.executeUpdate(sql);
            }
            System.out.println("Добавлено 30 элементов заказов");
        } catch (SQLException e) {
            System.out.println("Не удалось добавить элементы заказов: " + e.getMessage());
        }
    }

    public void selectProducts() throws SQLException {
        ResultSet resultSet;
        try (Statement statement = getDBConnection().createStatement()) {
            resultSet = statement.executeQuery("SELECT * FROM Products");
            System.out.println("\nТовары:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                System.out.printf("%d. %s - $%.2f\n", id, name, price);
            }
        }
    }

    public void selectCustomers() throws SQLException {
        ResultSet resultSet;
        try (Statement statement = getDBConnection().createStatement()) {
            resultSet = statement.executeQuery("SELECT * FROM Customers");
            System.out.println("\nКлиенты:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                System.out.printf("%d. %s - %s\n", id, name, email);
            }
        }
    }

    public void selectOrders() throws SQLException {
        ResultSet resultSet;
        try (Statement statement = getDBConnection().createStatement()) {
            resultSet = statement.executeQuery("SELECT * FROM Orders");
            System.out.println("\nЗаказы:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int customerId = resultSet.getInt("customer_id");
                Timestamp orderDate = resultSet.getTimestamp("order_date");
                System.out.printf("%d. Клиент ID: %d - Дата заказа: %s\n", id, customerId, orderDate);
            }
        }
    }

    public void selectOrderItems() throws SQLException {
        ResultSet resultSet;
        try (Statement statement = getDBConnection().createStatement()) {
            resultSet = statement.executeQuery("SELECT * FROM OrderItems");
            System.out.println("\nЭлементы заказов:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int orderId = resultSet.getInt("order_id");
                int productId = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");
                System.out.printf("%d. Заказ ID: %d - Продукт ID: %d - Количество: %d\n", id, orderId, productId, quantity);
            }
        }
    }
}

