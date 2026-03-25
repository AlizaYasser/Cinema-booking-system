package org.example.models;

public class Movie {
    private String title;
    private String genre;
    private String duration;
    private String rating;
    private String imagePath;

    public Movie(String title, String genre, String duration, String rating, String imagePath) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.imagePath = imagePath;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
    public String getRating() { return rating; }
    public String getImagePath() { return imagePath; }
}