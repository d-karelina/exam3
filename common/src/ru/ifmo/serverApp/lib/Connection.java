package ru.ifmo.serverApp.lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection implements AutoCloseable{
    //интерфейс AutoCloseable позволяет создавать объект в круглых скобках try
    private Socket socket ;
    private ObjectInputStream input ; //объекты в байт
    private ObjectOutputStream output ; //байты в объект

    public Connection(Socket socket) throws IOException {
        this.socket = socket ;
        //последовательность важна: сначала output потом input, иначе канал будет заблокинован входящим.
        output = new ObjectOutputStream(socket.getOutputStream()) ; //getOutputStream() - объект, который позволит по сокет-соединению отправлять данные из одной программы в другую
        input = new ObjectInputStream(socket.getInputStream()); //данные приходят из сокет-соединения
    }

    public void sendMessage (Message message) throws IOException {
        if (message.getDateTime() == null) message.setDateTime();
        output.writeObject(message);
        output.flush(); //байты из программы насильно передаются в поток - при работе с сетью. при работе с файлами вызывается внутри методов
    }

    public Message readMessage () throws IOException, ClassNotFoundException {
        return (Message) input.readObject() ;
    }

    @Override
    public void close() throws Exception {
        input.close() ;
        output.close() ;
        socket.close() ; //должен быть закрыт последним
    }
}