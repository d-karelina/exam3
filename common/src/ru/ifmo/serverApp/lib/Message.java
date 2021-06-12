package ru.ifmo.serverApp.lib;
// между собой они передают объект Message (implements Serializable)
// Message Свойства: отправитель, текст, дата
// Connection - взять готовый класс.

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String sender ;
    private String text ;
    private LocalDateTime dateTime ;

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        dateTime = LocalDateTime.now() ;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime() {
        this.dateTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
