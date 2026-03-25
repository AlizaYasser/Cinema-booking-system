package org.example.session;

public class SessionManager {

    private static int    currentUserId     = -1;
    private static String currentUsername   = "";
    private static int    currentCinemaId   = -1;
    private static String currentCinemaName = "";
    private static int    currentMovieId    = -1;
    private static String currentMovieName  = "";

    public static void setCurrentUserId(int id)           { currentUserId = id; }
    public static void setCurrentUsername(String name)    { currentUsername = name; }
    public static void setCurrentCinemaId(int id)         { currentCinemaId = id; }
    public static void setCurrentCinemaName(String name)  { currentCinemaName = name; }
    public static void setCurrentMovieId(int id)          { currentMovieId = id; }
    public static void setCurrentMovieName(String name)   { currentMovieName = name; }

    public static int    getCurrentUserId()      { return currentUserId; }
    public static String getCurrentUsername()    { return currentUsername; }
    public static int    getCurrentCinemaId()    { return currentCinemaId; }
    public static String getCurrentCinemaName()  { return currentCinemaName; }
    public static int    getCurrentMovieId()     { return currentMovieId; }
    public static String getCurrentMovieName()   { return currentMovieName; }

    public static void logout() {
        currentUserId     = -1;
        currentUsername   = "";
        currentCinemaId   = -1;
        currentCinemaName = "";
        currentMovieId    = -1;
        currentMovieName  = "";
    }
}