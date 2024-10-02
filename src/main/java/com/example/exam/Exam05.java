package com.example.exam;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Exam-05. ServerSocket에서 사용할 port를 현재 사용 중인 port로 지정해 동작을 확인해 보자.
public class Exam05 {
    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket = serverSocket.accept();

            socket.getOutputStream().write("Hello!\n".getBytes());
            socket.getOutputStream().flush();

            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
