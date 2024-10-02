package com.example;

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

// 클라이언트와의 통신을 처리하는 클래스
class ClientHandler implements Runnable {
    private final Socket clientSocket;

    // 생성자: 클라이언트 소켓을 초기화
    ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter socketOut = new BufferedWriter(
                        new OutputStreamWriter(clientSocket.getOutputStream()));) {

            String line;
            // 클라이언트로부터 한 줄씩 데이터를 읽어옴
            while ((line = socketIn.readLine()) != null) {
                System.out.println("Received from client[" + clientSocket.getInetAddress().getHostAddress() +
                        ":" + clientSocket.getPort() + "]: " + line);

                // 받은 데이터를 그대로 클라이언트에게 다시 전송 (Echo)
                socketOut.write(line + "\n");
                socketOut.flush();
            }
        } catch (IOException e) {
            System.err.println("클라이언트와의 통신 중 오류 발생: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client[" + clientSocket.getInetAddress().getHostAddress() +
                        ":" + clientSocket.getPort() + "]와의 연결을 종료합니다.");
            } catch (IOException e) {
                System.err.println("소켓을 닫는 중 오류 발생: " + e.getMessage());
            }
        }
    }
}
