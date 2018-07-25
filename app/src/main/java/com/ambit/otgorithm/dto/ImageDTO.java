package com.ambit.otgorithm.dto;

import java.util.HashMap;
import java.util.Map;

public class ImageDTO {
    public String description;
    public String imageUrl;
    public String email;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public ImageDTO(){

    }

    public ImageDTO(String description, String imageUrl, String email, int starCount, Map<String, Boolean> stars) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.email = email;
        this.starCount = starCount;
        this.stars = stars;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }
}
