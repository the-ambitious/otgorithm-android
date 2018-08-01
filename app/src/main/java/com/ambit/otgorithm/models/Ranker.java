package com.ambit.otgorithm.models;

public class Ranker {

    String profileImage;
    String nickName;
    String description;

    public Ranker(String profileImage, String nickName, String description) {
        this.profileImage = profileImage;
        this.nickName = nickName;
        this.description = description;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getNickName() {
        return nickName;
    }

    public String getDescription() {
        return description;
    }

}
