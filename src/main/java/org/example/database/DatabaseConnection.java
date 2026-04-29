package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:cinevision.db";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
            createTablesIfNotExist();
            System.out.println("Connected to SQLite successfully.");
            System.out.println("DB PATH: " + new java.io.File("cinevision.db").getAbsolutePath());
        }
        return connection;
    }

    private static void createTablesIfNotExist() {
        String cinemas = "CREATE TABLE IF NOT EXISTS Cinemas (" +
                "cinema_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cinema_name TEXT NOT NULL," +
                "location TEXT NOT NULL)";

        String movies = "CREATE TABLE IF NOT EXISTS Movies (" +
                "movie_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "movie_name TEXT NOT NULL," +
                "cinema_id INTEGER," +
                "show_date TEXT," +   // ADD THIS
                "showtime TEXT," +
                "price REAL," +
                "FOREIGN KEY (cinema_id) REFERENCES Cinemas(cinema_id))";

        String users = "CREATE TABLE IF NOT EXISTS Users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "full_name TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "role TEXT DEFAULT 'user')";

        String bookings = "CREATE TABLE IF NOT EXISTS Bookings (" +
                "booking_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "movie_id INTEGER," +
                "cinema_id INTEGER," +
                "seat_number TEXT NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES Users(user_id)," +
                "FOREIGN KEY (movie_id) REFERENCES Movies(movie_id)," +
                "FOREIGN KEY (cinema_id) REFERENCES Cinemas(cinema_id))";

        String insertCinemas = "INSERT OR IGNORE INTO Cinemas (cinema_id, cinema_name, location) VALUES " +
                "(1, 'CineVision Gold', 'Islamabad')," +
                "(2, 'CineVision Plus', 'Lahore')," +
                "(3, 'CineVision Max', 'Karachi')";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(cinemas);
            stmt.execute(movies);
            stmt.execute(users);
            stmt.execute(bookings);
            stmt.execute(insertCinemas);

            System.out.println("✅ Tables and sample data ready.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}