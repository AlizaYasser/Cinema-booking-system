package org.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CinemaDAO {

    // Returns list of all cinemas as String array [cinema_id, cinema_name, location]
    public static List<String[]> getAllCinemas() {
        List<String[]> cinemas = new ArrayList<>();
        String sql = "SELECT cinema_id, cinema_name, location FROM Cinemas";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cinemas.add(new String[]{
                        String.valueOf(rs.getInt("cinema_id")),
                        rs.getString("cinema_name"),
                        rs.getString("location")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cinemas;
    }
}