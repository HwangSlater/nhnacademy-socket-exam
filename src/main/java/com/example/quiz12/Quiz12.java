package com.example.quiz12;

// Quiz-12. Client에서 보내온 메시지를 접속한 모든 client에게 전송하는 broadcasting server를 만들어 보자.
public class Quiz12 {
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

        BroadCastingServer server = new BroadCastingServer(port);
        server.start();
    }
}
