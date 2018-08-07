package com.ambit.otgorithm.dto;

import java.util.HashMap;
import java.util.Map;

public class UserDTO {
    String email;
    String name;
    String uid;
    String profileUrl;
    String battlefield;

    // description을 추가(FavoritesActivity)


    public String token;

    boolean selection;


    public UserDTO(){

    }

    public UserDTO(String uid, String email, String name, String profileUrl) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;

    }

    public UserDTO(String email, String name, String uid, String profileUrl, String battlefield, String token, boolean selection) {
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.profileUrl = profileUrl;
        this.battlefield = battlefield;
        this.token = token;
        this.selection = selection;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getBattlefield() {
        return battlefield;
    }

    public void setBattlefield(String battlefield) {
        this.battlefield = battlefield;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }
}
