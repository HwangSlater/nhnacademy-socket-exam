package com.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// Quiz-10. 반복 연결이 가능한 Echo Server를 만들어 보자.
public class Quiz10 {
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
            // 무한 루프를 돌며 클라이언트 연결을 지속적으로 수락
            while (true) {
                // 소켓 생성
                Socket socket = serverSocket.accept();
                System.out.println("Client[" + socket.getInetAddress().getHostAddress() +
                        ":" + socket.getPort() + "]가 연결되었습니다.");

                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String line;
                while ((line = socketIn.readLine()) != null) {
                    System.out.println("Received from client[" + socket.getInetAddress().getHostAddress() +
                            ":" + socket.getPort() + "]: " + line);

                    socketOut.write(line + "\n");
                    socketOut.flush();
                }

                socket.close();
            }
        } catch (IOException e) {
            System.err.println("서버 소켓을 열 수 없습니다: " + e.getMessage());
        }
    }
}
