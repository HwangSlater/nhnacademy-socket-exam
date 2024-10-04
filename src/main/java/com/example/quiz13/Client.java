package com.example.quiz13;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    String id;
    String host;
    int port;

    public Client() {
        id = "user";
        host = "localhost";
        port = 1234;
    }

    public Client(String id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port);
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            // 클라이언트 ID를 서버에 전송
            socketOut.write(id + "가 접속했습니다.\n");
            socketOut.flush();

            // 사용자로부터 입력을 받아 서버로 메시지를 전송하는 스레드 생성
            Thread sendThread = new Thread(() -> {
                try {
                    String userMessage;
                    while ((userMessage = userInput.readLine()) != null) {
                        socketOut.write(userMessage + "\n");
                        socketOut.flush();
                    }
                } catch (IOException e) {
                    System.err.println("서버와의 연결이 끊어졌습니다: " + e.getMessage());
                }
            });
            sendThread.start();

            // 서버로부터 메시지를 받음
            String message;
            while ((message = socketIn.readLine()) != null) {
                if (message.equals("exit")) {
                    break;
                }
                System.out.println(message);
            }

        } catch (IOException e) {
            System.err.println("서버에 연결할 수 없습니다: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // 별도의 지정이 없을 경우
        if (args.length == 0) {
            System.out.println("임의의 id, host, port로 연결합니다.");
            Client client = new Client();
            Thread thread = new Thread(client);
            System.out.println(client.id);
            thread.start();

            // 별도의 지정이 있을 경우
        } else if (args.length == 3) {
            try {
                Thread thread = new Thread(new Client(args[0], args[1], Integer.parseInt(args[2])));
                thread.start();
            } catch (NumberFormatException e) {
                System.err.println("올바른 형식의 포트가 아닙니다.");
                System.exit(1);
            }

        } else {
            System.out.println("올바른 형식을 입력해주세요");
            System.exit(1);
        }

    }
}
