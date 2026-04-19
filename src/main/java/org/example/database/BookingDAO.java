package org.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // Save a booking — returns true if success
    public static boolean saveBooking(int userId, int movieId,
                                      int cinemaId, String seatNumber) {
        // First check if seat is already booked for this movie
        if (isSeatBooked(movieId, seatNumber)) {
            System.out.println("⚠️ Seat already booked.");
            return false;
        }

        String sql = "INSERT INTO Bookings (user_id, movie_id, cinema_id, seat_number) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);
            stmt.setInt(3, cinemaId);
            stmt.setString(4, seatNumber);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if a seat is already booked for a movie
    public static boolean isSeatBooked(int movieId, String seatNumber) {
        String sql = "SELECT booking_id FROM Bookings " +
                "WHERE movie_id = ? AND seat_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, movieId);
            stmt.setString(2, seatNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all booked seats for a movie
    public static List<String> getBookedSeats(int movieId) {
        List<String> seats = new ArrayList<>();
        String sql = "SELECT seat_number FROM Bookings WHERE movie_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                seats.add(rs.getString("seat_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    // Get booking history for a user
    public static List<String[]> getBookingHistory(int userId) {
        List<String[]> history = new ArrayList<>();

        String sql =
                "SELECT b.booking_id, m.movie_name, c.cinema_name, b.seat_number, m.showtime " +
                        "FROM Bookings b " +
                        "JOIN Movies m ON b.movie_id = m.movie_id " +
                        "JOIN Cinemas c ON b.cinema_id = c.cinema_id " +
                        "WHERE b.user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                history.add(new String[]{
                        String.valueOf(rs.getInt("booking_id")), // IMPORTANT
                        rs.getString("movie_name"),
                        rs.getString("cinema_name"),
                        rs.getString("seat_number"),
                        rs.getString("showtime")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history;
    }

    public static int getTotalBookings() {
        String sql = "SELECT COUNT(*) AS total FROM Bookings";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static double getTotalRevenue() {
        String sql =
                "SELECT SUM(m.price) AS revenue " +
                        "FROM Bookings b " +
                        "JOIN Movies m ON b.movie_id = m.movie_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("revenue");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public static boolean cancelBooking(int bookingId) {
        String sql = "DELETE FROM Bookings WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}