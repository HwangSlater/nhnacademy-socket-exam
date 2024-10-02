package com.example.quiz11;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// Quiz-11. 하나 이상의 client가 동시 연결될 수 있도록 echo server를 구성해 보자.
public class Quiz11 {
    public static void main(String[] args) {
        int port = 1234;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("잘못된 형식의 포트입니다.");
                System.exit(1);
            }
        }

        // 서버 소켓 생성 및 클라이언트 연결 대기
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Echo Server가 포트 " + port + "에서 실행 중입니다.");

            // 무한 루프를 돌며 클라이언트 연결을 지속적으로 수락
            while (true) {
                try {
                    // 소켓 생성
                    Socket socket = serverSocket.accept();
                    System.out.println("Client[" + socket.getInetAddress().getHostAddress() +
                            ":" + socket.getPort() + "]가 연결되었습니다.");

                    // 하나의 클라이언트만이 아닌 여러 개의 클라이언트가 접속돼도 기능이 수행되도록 스레드를 만들어서 실행
                    // 스레드를 안만들면 하나의 클라이언트만 처리할 수 밖에 없음
                    ClientHandler clientHandler = new ClientHandler(socket);
                    Thread thread = new Thread(clientHandler);
                    thread.start();

                } catch (IOException e) {
                    System.err.println("클라이언트 연결 중 오류 발생: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("서버 소켓을 열 수 없습니다: " + e.getMessage());
        }
    }
}
