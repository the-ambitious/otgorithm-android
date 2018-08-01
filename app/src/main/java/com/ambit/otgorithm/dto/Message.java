package com.ambit.otgorithm.dto;

import java.util.Date;
import java.util.List;

public class Message {
    private String messageId;
    private UserDTO messageUser;
    private String chatId;
    private int unreadCount;
    private Date messageDate;
    private MessageType messageType;
    private List<String> readUserList;

    public enum MessageType {
        TEXT, PHOTO, EXIT
    }

    public Message(){

    }

    public Message(String messageId, UserDTO messageUser, String chatId, int unreadCount, Date messageDate, MessageType messageType, List<String> readUserList) {
        this.messageId = messageId;
        this.messageUser = messageUser;
        this.chatId = chatId;
        this.unreadCount = unreadCount;
        this.messageDate = messageDate;
        this.messageType = messageType;
        this.readUserList = readUserList;
    }


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public UserDTO getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(UserDTO messageUser) {
        this.messageUser = messageUser;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public List<String> getReadUserList() {
        return readUserList;
    }

    public void setReadUserList(List<String> readUserList) {
        this.readUserList = readUserList;
    }
}
