package ru.ifmo.serverApp;
// Сервер
// на каждое подключение выделяется отдельный поток. этот поток будет заниматься только получением сообщения от клиента
// после получения сообщения, он добавляет его в блокирующую очередь (коллекцию).
// Объект соединения с этим клиентом тоже нужно хранить в коллекции (потокобезопасный HashSet)
// +
// ещё только один поток - Отправитель:
// забирает сообщение из блокирующей очереди и рассылает сообщение всем клиентам, кроме отправителя

import ru.ifmo.serverApp.lib.Connection;
import ru.ifmo.serverApp.lib.Message;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

// Для архитектуры сервера и клиента использовать внутренние (не статик) классы - для классов потоков,
// чтобы иметь доступ к свойствам внешнего класса.
public class Server {
    private final int port ;
    private ConcurrentHashMap<Connection, String> clients ;
    private BlockingQueue<Message> messages = new LinkedBlockingQueue<>() ;


    public  Server(int port) {
        this.port = port ;
        clients = new ConcurrentHashMap<>() ;

    }

    ExecutorService fixedPool = Executors.newFixedThreadPool(2) ;

    public void start () {
        Thread sender = new ServerSender() ;
        try (ServerSocket serverSocket = new ServerSocket(port)){  //Ожидание клиентских подключений
            System.out.println("сервер запущен") ;

            // запускаем поток, рассылающий сообщения из очереди
            sender.start() ;

            while (!Thread.currentThread().isInterrupted()) {
                Socket newClient = serverSocket.accept() ;
                fixedPool.execute(new ClientHandler(newClient)) ;

            }
            //fixedPool.shutdown() ;
        } catch (IOException e) {
            System.out.println("Ошибка сервера");
        }
    }


    class ClientHandler implements Runnable {
        private final Socket clientDialog ;

        public ClientHandler(Socket socket) {
            clientDialog = socket ;
        }

        @Override
        public void run() {
            try {
                Connection connection = new Connection(clientDialog) ;
                System.out.println("соединение установлено") ;
                while (true) {
                    Message message = connection.readMessage() ;
                    if (message.getText().equals("exit")) {
                        System.out.println("до свидания") ;
                        clients.remove(connection) ;
                        connection.close() ;
                        break ;
                    }
                    clients.put(connection, message.getSender());
                    messages.add(message);
                    System.out.println("всё записано");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ServerSender extends Thread {

        @Override
        public void run() {
            while (messages.isEmpty()) {
                try {
                    // берем из очереди первую запись
                    Message toSend = messages.take() ;
                    // перебираем мапу с соединениями,
                    for (Map.Entry<Connection, String> entry: clients.entrySet()) {
                        // если запись не содержит сообщения...
                        if (!(entry.getValue().contains(toSend.getSender())))
                        {
                            try {
                                // отправляем сообщение по соединению, которое хранится в ключе записи
                                entry.getKey().sendMessage(toSend) ;
                            } catch (IOException e) {
                                clients.remove(entry.getKey()) ;
                            }
                        }
                    }
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }

        }
    }

}
