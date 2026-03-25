package org.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    // Returns movies for a specific cinema as String array
    // [movie_id, movie_name, showtime, price]
    public static List<String[]> getMoviesByCinema(int cinemaId) {
        List<String[]> movies = new ArrayList<>();
        String sql = "SELECT movie_id, movie_name, showtime, price " +
                "FROM Movies WHERE cinema_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cinemaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                movies.add(new String[]{
                        String.valueOf(rs.getInt("movie_id")),
                        rs.getString("movie_name"),
                        rs.getString("showtime"),
                        String.valueOf(rs.getDouble("price"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
}