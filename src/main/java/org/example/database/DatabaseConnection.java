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
            System.out.println("✅ Connected to SQLite successfully.");
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
                "showtime TEXT," +
                "price REAL," +
                "FOREIGN KEY (cinema_id) REFERENCES Cinemas(cinema_id))";

        String users = "CREATE TABLE IF NOT EXISTS Users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "full_name TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL)";

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

        String insertMovies = "INSERT OR IGNORE INTO Movies (movie_id, movie_name, cinema_id, showtime, price) VALUES " +
                "(1, 'Avengers: Endgame', 1, '06:00 PM', 850.0)," +
                "(2, 'Inception', 1, '09:00 PM', 900.0)," +
                "(3, 'The Dark Knight', 1, '03:00 PM', 850.0)," +
                "(4, 'Interstellar', 2, '07:00 PM', 900.0)," +
                "(5, 'Titanic', 2, '04:00 PM', 800.0)," +
                "(6, 'The Lion King', 2, '01:00 PM', 750.0)," +
                "(7, 'Spider-Man', 3, '05:00 PM', 850.0)," +
                "(8, 'Doctor Strange', 3, '08:00 PM', 900.0)," +
                "(9, 'Black Panther', 3, '02:00 PM', 800.0)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(cinemas);
            stmt.execute(movies);
            stmt.execute(users);
            stmt.execute(bookings);
            stmt.execute(insertCinemas);
            stmt.execute(insertMovies);

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