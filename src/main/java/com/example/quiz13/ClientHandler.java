package com.example.quiz13;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

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