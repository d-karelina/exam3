package ru.ifmo.serverApp.clientApp;

import ru.ifmo.serverApp.lib.Connection;
import ru.ifmo.serverApp.lib.Message;

import java.io.IOException;


public class ClientReader extends Thread{

    Connection connection ;

    public ClientReader(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message fromServer = connection.readMessage() ;
                System.out.println("от сервера: " + fromServer) ;
            } catch (IOException | ClassNotFoundException e ){
                e.printStackTrace();
                this.interrupt() ;
                try {
                    connection.close() ;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
