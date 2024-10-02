package com.example;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

// Exam-02. Client socket을 server에 연결하여 data를 보낸다
public class Exam02 {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1234;

        // Exam02파일을 터미널에서 따로 실행할 때 host와 port를 주면 기존의 host와 port는 바뀜.
        // Ex) java Exam02.java localhost 8080

        // 호스트만 입력했을 경우를 위해
        if (args.length > 0) {
            host = args[0];
        }

        // 포트까지 입력한 경우
        try {
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException e) {
            System.err.println("Port가 올바르지 않습니다.");
            System.exit(1);
        }

        try {
            Socket socket = new Socket(host, port);

            System.out.println("서버에 연결되었습니다.");

            // 연결된 socket에서 output stream을 얻어서 데이터를 전송.
            // 이때 write함수는 byte[]를 전송하므로 문자열 byte[]로 변환
            socket.getOutputStream().write("Hello World!\n".getBytes());

            socket.close();
        } catch (UnknownHostException e) {
            System.err.println(host + ": " + port + "에 연결할 수 없습니다.");
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
