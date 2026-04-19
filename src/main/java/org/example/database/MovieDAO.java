package org.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    // ─────────────────────────────────────────────
    // GET MOVIES BY CINEMA
    // ─────────────────────────────────────────────
    public static List<String[]> getMoviesByCinema(int cinemaId) {
        List<String[]> movies = new ArrayList<>();
        String sql = "SELECT movie_id, movie_name, show_date, showtime, price FROM Movies WHERE cinema_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cinemaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                movies.add(new String[]{
                        String.valueOf(rs.getInt("movie_id")),
                        rs.getString("movie_name"),
                        rs.getString("show_date"),
                        rs.getString("showtime"),
                        String.valueOf(rs.getDouble("price"))
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static List<String[]> getAllMovies() {
        List<String[]> movies = new ArrayList<>();
        String sql = "SELECT movie_id, movie_name, show_date, showtime, price FROM Movies";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                movies.add(new String[]{
                        String.valueOf(rs.getInt("movie_id")),
                        rs.getString("movie_name"),
                        rs.getString("show_date"),
                        rs.getString("showtime"),
                        String.valueOf(rs.getDouble("price"))
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    // ─────────────────────────────────────────────
    // ADD MOVIE (ADMIN)
    // ─────────────────────────────────────────────
    public static boolean addMovie(String name, int cinemaId,
                                   String showDate, String showtime,
                                   double price) {

        String sql = "INSERT INTO Movies (movie_name, cinema_id, show_date, showtime, price) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, cinemaId);
            stmt.setString(3, showDate);
            stmt.setString(4, showtime);
            stmt.setDouble(5, price);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("MovieDAO.addMovie error: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────────────────────
    // DELETE MOVIE (ADMIN)
    // ─────────────────────────────────────────────
    public static boolean deleteMovie(int movieId) {
        String sql = "DELETE FROM Movies WHERE movie_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, movieId);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("MovieDAO.deleteMovie error: " + e.getMessage());
            return false;
        }
    }

    public static List<String[]> getRecentMovies(int limit) {
        List<String[]> movies = new ArrayList<>();

        String sql = "SELECT movie_name, show_date, showtime, price " +
                "FROM Movies " +
                "ORDER BY movie_id DESC " +
                "LIMIT ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                movies.add(new String[]{
                        rs.getString("movie_name"),
                        rs.getString("show_date"),
                        rs.getString("showtime"),
                        String.valueOf(rs.getDouble("price"))
                });
            }

        } catch (SQLException e) {
            System.out.println("MovieDAO.getRecentMovies error: " + e.getMessage());
        }

        return movies;
    }
}