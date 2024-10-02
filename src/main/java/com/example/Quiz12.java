package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// Quiz-12. Client에서 보내온 메시지를 접속한 모든 client에게 전송하는 broadcasting server를 만들어 보자.
public class Quiz12 {
    public static void main(String[] args) {
        int port = 1234;

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            System.err.println("잘못된 형식의 포트입니다.");
            System.exit(port);
        }

        BroadCastingServer server = new BroadCastingServer(port);
        server.start();
    }
}

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

class ClientHandler implements Runnable {
    Socket clientSocket;
    BroadCastingServer server;
    BufferedWriter output;

    ClientHandler(Socket socket, BroadCastingServer server) {
        clientSocket = socket;
        this.server = server;
        try {
            output = new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            System.err.println("클라이언트 스트림 초기화 중 오류: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try (BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter terminalOut = new BufferedWriter(new OutputStreamWriter(System.out))) {

            String line;
            while ((line = socketIn.readLine()) != null) {
                server.sendMessage(line.trim(), this);
                terminalOut.write(line.trim() + "\n");
                terminalOut.flush();
            }

            server.removeClient(this);
            clientSocket.close();
            output.close();
        } catch (IOException e) {
            System.err.println("클라이언트와의 통신 중 오류: " + e.getMessage());
        }
    }

    public void receivedMessage(String message) {
        try {
            output.write(message + "\n");
            output.flush();
        } catch (IOException e) {
            System.err.println("메시지 전송 중 오류: " + e.getMessage());
        }
    }
}
