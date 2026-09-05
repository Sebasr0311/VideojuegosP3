package com.gamezone.model;

import java.util.Objects;

/**
 * Concrete domain entity representing a video game.
 * Specializes Product with platform, genre, and ESRB/recommended age rating.
 */
public class VideoGame extends Product {
    private String platform;
    private String genre;
    private String ageRating;

    public VideoGame(String id, String title, double price, int stock,
                     String platform, String genre, String ageRating) {
        super(id, title, price, stock);
        if (platform == null || platform.trim().isEmpty()) {
            throw new IllegalArgumentException("Video game platform cannot be empty.");
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Video game genre cannot be empty.");
        }
        if (ageRating == null || ageRating.trim().isEmpty()) {
            throw new IllegalArgumentException("Age rating cannot be empty.");
        }
        this.platform = platform.trim();
        this.genre = genre.trim();
        this.ageRating = ageRating.trim();
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        if (platform == null || platform.trim().isEmpty()) {
            throw new IllegalArgumentException("Platform cannot be empty.");
        }
        this.platform = platform.trim();
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty.");
        }
        this.genre = genre.trim();
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        if (ageRating == null || ageRating.trim().isEmpty()) {
            throw new IllegalArgumentException("Age rating cannot be empty.");
        }
        this.ageRating = ageRating.trim();
    }

    @Override
    public String getDescription() {
        return String.format("[Video Game] ID: %s | Title: %s | Platform: %s | Genre: %s | Rating: %s | Price: $%.2f | Stock: %d",
                getId(), getTitle(), platform, genre, ageRating, getPrice(), getStock());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VideoGame videoGame = (VideoGame) o;
        return Objects.equals(platform, videoGame.platform);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), platform);
    }
}