package com.example.exam;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Exam-04. ServerSocket을 생성하여 client와 연결해 보자.
public class Exam04 {
    public static void main(String[] args) {
        int port = 1234;

        /*
         * 이전에는 터미널에서 서버 역할을 했으나,
         * 이번에는 VSCode에서 실행하여 서버를 실행한다.
         * 지정한 포트에 서버를 생성한다.
         */
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // 클라이언트 연결 대기
            Socket socket = serverSocket.accept();

            // 연결이 정상적으로 되면 서버가 소켓을 사용하여 클라이언트에게 메시지를 보냄
            socket.getOutputStream().write("Hello!\n".getBytes());
            socket.getOutputStream().flush();

            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
