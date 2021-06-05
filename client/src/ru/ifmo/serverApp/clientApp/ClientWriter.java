package ru.ifmo.serverApp.clientApp;

import ru.ifmo.serverApp.lib.Connection;
import ru.ifmo.serverApp.lib.Message;

import java.io.IOException;
import java.util.Scanner;

public class ClientWriter extends Thread {
    Connection connection ;

    public ClientWriter(Connection connection) {
        this.connection = connection ;
    }

    public void createAndSendMessage() {
        System.out.println("Введите имя");
        Scanner scanner = new Scanner(System.in) ;
        String userName = scanner.nextLine();
        System.out.println("Введите текст сообщения");
        String messageText = scanner.nextLine();

        try {
            Message message = new Message(userName, messageText) ;
            connection.sendMessage(message) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            createAndSendMessage() ;
        }
    }
}
