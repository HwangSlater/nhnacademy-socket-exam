package com.example.exam;

import java.io.IOException;
import java.net.Socket;

// Exam-03. Client socket을 server에 연결하여 data를 받는다
public class Exam03 {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1234;

        try {
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException ignore) {
            System.err.println("Port가 올바르지 않습니다.");
            System.exit(1);
        }

        try {
            Socket socket = new Socket(host, port);
            System.out.println("서버에 연결되었습니다.");

            int ch;
            while ((ch = socket.getInputStream().read()) >= 0) {
                System.out.write(ch);
            }

            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
