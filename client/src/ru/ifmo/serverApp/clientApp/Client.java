package ru.ifmo.serverApp.clientApp;
// На клиенте 2 потока:
// 1 поток - инструкции для отправления данных на сервер
// 2 поток данные с сервера получает
// Клиент устанавливает соединение с сервером и вообще его не разрывает


import ru.ifmo.serverApp.lib.Connection;

import java.io.IOException;
import java.net.Socket;

public class Client {
    protected final String ip ;
    protected final int port ;
    private final Connection connection ;

    public Client(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        Socket socket = new Socket(ip, port) ;
        connection = new Connection(socket) ;
    }

    public void start () {
        Thread clientWriter = new ClientWriter(connection) ;
        clientWriter.start() ;
        Thread clientReader = new ClientReader(connection) ;
        clientReader.start() ;
    }

}
