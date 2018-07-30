package com.ambit.otgorithm.dto;

import java.util.Date;
import java.util.List;

public class PhotoMessage extends Message {
    private String photoUrl;

    public PhotoMessage(){

    }

    public PhotoMessage(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public PhotoMessage(String messageId, UserDTO messageUser, String chatId, int unreadCount, Date messageDate, MessageType messageType, List<String> readUserList, String photoUrl) {
        super(messageId, messageUser, chatId, unreadCount, messageDate, messageType, readUserList);
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

