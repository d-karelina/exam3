package ru.ifmo.serverApp.clientApp;

import java.io.IOException;
import java.net.UnknownHostException;

public class ClientApp2 {
    public static void main(String[] args) {
        try {
            new Client("127.0.0.1", 8999).start();
        } catch (UnknownHostException e) {
            System.out.println("Не смог соединиться..." + e.getMessage());
        } catch (IOException e) {
            System.out.println("Не найден класс Message" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
