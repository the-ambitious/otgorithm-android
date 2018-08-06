package com.ambit.otgorithm.models;

public class Sender {
    public Notification notification;
    public Data data;
    public String to;

    public Sender() {
    }

    public Sender(String to,Notification notification) {
        this.notification = notification;
        this.to = to;
    }

    public Sender(String to,Data data) {
        this.data = data;
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
