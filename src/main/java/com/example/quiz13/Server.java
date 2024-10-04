package com.example.quiz13;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class Server {
    int port;
    List<ClientHandler> clientList = Collections.synchronizedList(new LinkedList<>());
    ConcurrentHashMap<String, ClientHandler> idMap = new ConcurrentHashMap<>();

    Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                Socket socket = server.accept();
                System.out.println("Client[" + socket.getInetAddress().getHostAddress() +
                        ":" + socket.getPort() + "]가 연결되었습니다.");

                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientList.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("서버 오류: " + e.getMessage());
        }
    }

    public void sendMessage(String message, ClientHandler clientHandler) {
        for (ClientHandler client : clientList) {
            if (!client.equals(clientHandler)) {
                client.receivedMessage(message);
            }
        }
    }

    public void sendWhisper(String message, ClientHandler clientHandler) {
        clientHandler.receivedMessage(message);
    }

    public void removeClient(ClientHandler clientHandler) {
        clientList.remove(clientHandler);
    }
}