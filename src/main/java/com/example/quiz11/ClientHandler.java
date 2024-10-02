package com.example.quiz11;

import java.io.*;
import java.net.Socket;

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
                System.out.println("Received from client[" +
                        clientSocket.getInetAddress().getHostAddress() +
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
                System.out.println("Client[" + clientSocket.getInetAddress().getHostAddress()
                        +
                        ":" + clientSocket.getPort() + "]와의 연결을 종료합니다.");
            } catch (IOException e) {
                System.err.println("소켓을 닫는 중 오류 발생: " + e.getMessage());
            }
        }
    }
}