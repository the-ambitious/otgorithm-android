package com.ambit.otgorithm.dto;

public class UserDTO {
    String email;
    String name;
    String uid;
    String profileUrl;
    String battlefield;
    boolean selection;


    public UserDTO(){

    }

    public UserDTO(String uid, String email, String name, String profileUrl) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;
    }

    public UserDTO(String email, String name, String uid, String profileUrl, String battlefield, boolean selection) {
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.profileUrl = profileUrl;
        this.battlefield = battlefield;
        this.selection = selection;
    }

    public String getBattlefield() {
        return battlefield;
    }

    public void setBattlefield(String battlefield) {
        this.battlefield = battlefield;
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

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }
}
