package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Quiz-08. Server socket에서 client 접속 후 생성된 socket을 이용해 client 정보(host, port)를 확인하자
public class Quiz08 {
    public static void main(String[] args) {
        int port = 1234;

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            System.err.println("포트 형식이 올바르지 않습니다.");
            System.exit(1);
        }

        // vscode에서 서버 역할 수행
        try (ServerSocket server = new ServerSocket(port)) {
            Socket socket = server.accept();
            // 원격 머신의 IP주소 반환
            System.out.println("Client[" + socket.getInetAddress().getHostAddress() +
                    ":" + socket.getPort() + "]가 연결되었습니다.");

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
