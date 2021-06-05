package ru.ifmo.serverApp.clientApp;
// На клиенте 2 потока:
// 1 поток - инструкции для отправления данных на сервер
// 2 поток данные с сервера получает
// Клиент устанавливает соединение с сервером и вообще его не разрывает


import ru.ifmo.serverApp.lib.Connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    protected final String ip ;
    protected final int port ;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start () {
        try (Connection connection = new Connection(new Socket(ip, port))) {
            Thread clientWriter = new ClientWriter(connection) ;
            clientWriter.start();
            // Thread clientReader = new ClientReader(connection) ;
            // clientReader.start() ;
        } catch (UnknownHostException e) {
            System.out.println("Не смог соединиться..." + e.getMessage());
        } catch (IOException e) {
            System.out.println("Не найден класс Message" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
