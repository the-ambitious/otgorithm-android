package com.ambit.otgorithm.dto;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserDTO {
    public String email;
    public String name;
    public String uid;
    public String profileUrl;
    public String battlefield;
    public String description;
    public String token;
    public boolean selection;


    public UserDTO(){

    }

    public UserDTO(String uid, String email, String name, String profileUrl) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;

    }

    public UserDTO(String email, String name, String uid, String profileUrl, String battlefield, String description, String token, boolean selection) {
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.profileUrl = profileUrl;
        this.battlefield = battlefield;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("email",email);
        result.put("name",name);
        result.put("uid",uid);
        result.put("profileUrl",profileUrl);
        result.put("battlefield",battlefield);
        result.put("description",description);
        result.put("token",token);
        result.put("selection",selection);

        return result;
    }



}
