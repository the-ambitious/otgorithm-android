package com.ambit.otgorithm.dto;

import java.util.Date;
import java.util.List;

public class TextMessage  extends Message{
    private String messageText;

    public TextMessage(){

    }

    public TextMessage(String messageText) {
        this.messageText = messageText;
    }

    public TextMessage(String messageId, UserDTO messageUser, String chatId, int unreadCount, Date messageDate, MessageType messageType, List<String> readUserList, String messageText) {
        super(messageId, messageUser, chatId, unreadCount, messageDate, messageType, readUserList);
        this.messageText = messageText;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
