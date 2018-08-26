package com.ambit.otgorithm.dto;

import java.util.HashMap;
import java.util.Map;

public class GalleryDTO {

    public String nickname;
    public String description;
    public String imageUrl;
    public String email;
    public String sysdate;
    public String battlefield;
    public String gid;
    public int weather;
    public int weatherIcon;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
    public int accusationCount = 0;
    public Map<String, Boolean> accusations = new HashMap<>();
    public GalleryDTO() {
    }

    public GalleryDTO(String nickname, String description, String imageUrl, String email, String sysdate, String battlefield, String gid, int weather, int weatherIcon, int starCount, Map<String, Boolean> stars) {
        this.nickname = nickname;
        this.description = description;
        this.imageUrl = imageUrl;
        this.email = email;
        this.sysdate = sysdate;
        this.battlefield = battlefield;
        this.gid = gid;
        this.weather = weather;
        this.weatherIcon = weatherIcon;
        this.starCount = starCount;
        this.stars = stars;
    }

    public GalleryDTO(String nickname, String description, String imageUrl, String email, String sysdate, String battlefield, String gid, int weather, int weatherIcon, int starCount, Map<String, Boolean> stars, int accusationCount, Map<String, Boolean> accusations) {
        this.nickname = nickname;
        this.description = description;
        this.imageUrl = imageUrl;
        this.email = email;
        this.sysdate = sysdate;
        this.battlefield = battlefield;
        this.gid = gid;
        this.weather = weather;
        this.weatherIcon = weatherIcon;
        this.starCount = starCount;
        this.stars = stars;
        this.accusationCount = accusationCount;
        this.accusations = accusations;
    }

    public int getAccusationCount() {
        return accusationCount;
    }

    public void setAccusationCount(int accusationCount) {
        this.accusationCount = accusationCount;
    }

    public Map<String, Boolean> getAccusations() {
        return accusations;
    }

    public void setAccusations(Map<String, Boolean> accusations) {
        this.accusations = accusations;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getSysdate() {
        return sysdate;
    }

    public void setSysdate(String sysdate) {
        this.sysdate = sysdate;
    }

    public String getBattlefield() {
        return battlefield;
    }

    public void setBattlefield(String battlefield) {
        this.battlefield = battlefield;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
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
