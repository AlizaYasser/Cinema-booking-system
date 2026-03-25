package org.example.booking;

import org.example.database.BookingDAO;
import org.example.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingManager – Person 3
 * Handles all booking logic:
 *  - Confirms bookings for one or more seats
 *  - Prevents double-booking (seat already taken)
 *  - Performs full error handling (invalid input, DB failure)
 *  - Returns a structured BookingResult so the UI can react clearly
 */
public class BookingManager {

    // ------------------------------------------------------------------ //
    //  Inner result class – keeps UI and logic cleanly separated
    // ------------------------------------------------------------------ //
    public static class BookingResult {
        public enum Status { SUCCESS, SEAT_TAKEN, INVALID_INPUT, DB_ERROR }

        public final Status  status;
        public final String  message;
        /** Seats that were successfully saved (may be a subset if partial failure) */
        public final List<String> bookedSeats;
        /** The seat that caused a failure (if any) */
        public final String  failedSeat;

        private BookingResult(Status status, String message,
                              List<String> bookedSeats, String failedSeat) {
            this.status      = status;
            this.message     = message;
            this.bookedSeats = bookedSeats;
            this.failedSeat  = failedSeat;
        }

        public boolean isSuccess() { return status == Status.SUCCESS; }
    }

    // ------------------------------------------------------------------ //
    //  confirmBooking – main entry point
    // ------------------------------------------------------------------ //
    /**
     * Attempts to book all requested seats atomically.
     * If ANY seat is already taken the whole booking is rejected before
     * touching the database (pre-validation), so no partial state is stored.
     *
     * @param userId    logged-in user
     * @param movieId   selected movie
     * @param cinemaId  selected cinema
     * @param seats     one or more seat identifiers, e.g. ["A1","B3"]
     * @return          BookingResult describing outcome
     */
    public static BookingResult confirmBooking(int userId, int movieId,
                                               int cinemaId, List<String> seats) {

        // --- 1. Input validation ---
        if (userId <= 0 || movieId <= 0 || cinemaId <= 0) {
            return new BookingResult(
                    BookingResult.Status.INVALID_INPUT,
                    "Invalid session data. Please log in again.",
                    new ArrayList<>(), null);
        }

        if (seats == null || seats.isEmpty()) {
            return new BookingResult(
                    BookingResult.Status.INVALID_INPUT,
                    "No seats selected. Please choose at least one seat.",
                    new ArrayList<>(), null);
        }

        for (String seat : seats) {
            if (seat == null || seat.trim().isEmpty()) {
                return new BookingResult(
                        BookingResult.Status.INVALID_INPUT,
                        "One or more seat identifiers are empty.",
                        new ArrayList<>(), null);
            }
        }

        // --- 2. DB connectivity check ---
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null || conn.isClosed()) {
                return new BookingResult(
                        BookingResult.Status.DB_ERROR,
                        "Cannot connect to the database. Please try again later.",
                        new ArrayList<>(), null);
            }
        } catch (SQLException e) {
            System.err.println("[BookingManager] DB connection check failed: " + e.getMessage());
            return new BookingResult(
                    BookingResult.Status.DB_ERROR,
                    "Database connection failure: " + e.getMessage(),
                    new ArrayList<>(), null);
        }

        // --- 3. Pre-check all seats for double-booking BEFORE saving anything ---
        for (String seat : seats) {
            try {
                if (BookingDAO.isSeatBooked(movieId, seat)) {
                    return new BookingResult(
                            BookingResult.Status.SEAT_TAKEN,
                            "Seat " + seat + " is already booked. Please choose a different seat.",
                            new ArrayList<>(), seat);
                }
            } catch (Exception e) {
                System.err.println("[BookingManager] Error checking seat " + seat + ": " + e.getMessage());
                return new BookingResult(
                        BookingResult.Status.DB_ERROR,
                        "Error verifying seat availability. Please try again.",
                        new ArrayList<>(), seat);
            }
        }

        // --- 4. Save all seats (all passed the check, so save them) ---
        List<String> booked = new ArrayList<>();
        for (String seat : seats) {
            boolean saved = BookingDAO.saveBooking(userId, movieId, cinemaId, seat);
            if (!saved) {
                // Race condition: another user just took this seat between check and save
                System.err.println("[BookingManager] Race-condition on seat " + seat);
                return new BookingResult(
                        BookingResult.Status.SEAT_TAKEN,
                        "Seat " + seat + " was just taken by another user! Please pick a new seat.",
                        booked, seat);
            }
            booked.add(seat);
        }

        return new BookingResult(
                BookingResult.Status.SUCCESS,
                "Booking confirmed for seat(s): " + String.join(", ", booked),
                booked, null);
    }
}
