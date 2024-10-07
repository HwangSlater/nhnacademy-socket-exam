package com.example.quiz13;

import java.io.*;
import java.net.Socket;

public class Client {
    private String id;
    private String host;
    private int port;

    public Client(String id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            // 클라이언트 ID를 서버에 전송
            out.write(id + "\n");
            out.flush();

            // 서버로부터 메시지를 받는 스레드
            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        // id가 중복이거나 !exit를 입력했을 경우 종료
                        if (message.equals("!exit")) {
                            System.exit(1);
                        }
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // 사용자 입력을 서버로 전송
            String userMessage;
            while ((userMessage = userInput.readLine()) != null) {
                out.write(userMessage + "\n");
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("id, host, port를 지정해주세요.");
            return;
        }

        Client client = new Client(args[0], "localhost", 1234);
        client.start();
    }
}
