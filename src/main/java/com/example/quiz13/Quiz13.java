package com.example.quiz13;

// Quiz-13. Client에서 보내온 메시지를 접속한 모든 client 또는 특정 client에 전송할 수 있도록 multi-chatting client/server를 만들어 보자.
public class Quiz13 {
    public static void main(String[] args) {
        int port = 1234;

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            System.err.println("잘못된 형식의 포트입니다.");
            System.exit(port);
        }

        new Server(port).start();
    }
}
