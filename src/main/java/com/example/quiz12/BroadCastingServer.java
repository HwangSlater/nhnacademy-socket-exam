package com.example.quiz12;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class BroadCastingServer {
    int port;
    List<ClientHandler> clientList = Collections.synchronizedList(new LinkedList<>());

    BroadCastingServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket broadCastingServer = new ServerSocket(port)) {
            while (true) {
                Socket socket = broadCastingServer.accept();
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

    public void removeClient(ClientHandler clientHandler) {
        clientList.remove(clientHandler);
    }
}
