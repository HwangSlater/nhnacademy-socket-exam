package com.example.quiz13;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final int port;
    private final ConcurrentHashMap<String, BufferedWriter> clientWriters = new ConcurrentHashMap<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("서버가 시작되었습니다. 포트: " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        String clientId = null; // 클라이언트 ID 초기화
        try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            clientId = input.readLine().trim(); // 클라이언트 ID 읽기

            // id가 중복일 경우
            if (clientWriters.containsKey(clientId)) {
                output.write("이미 접속 중인 ID입니다. 연결을 종료합니다.\n");
                output.flush();

                output.write("!exit");
                output.flush();

                return;
            } else {
                // id가 중복이 아닐 경우
                clientWriters.put(clientId, output); // 클라이언트 ID와 출력 스트림 매핑
                System.out.println(clientId + "가 연결했습니다.");
            }

            String line;
            while ((line = input.readLine()) != null) {
                // 귓속말 기능
                if (line.startsWith("@")) {
                    String[] parts = line.split(" ", 2);
                    if (parts.length == 2) {
                        String targetId = parts[0].substring(1); // '@' 기호 제거
                        sendWhisper(clientId, targetId, parts[1] + "\n");
                    } else {
                        output.write("귓속말 형식이 잘못되었습니다. 올바른 형식: @사용자 메시지\n");
                        output.flush();
                    }
                } else if (line.equals("!exit")) { // 제어 명령
                    output.write("연결을 종료합니다.\n");
                    output.flush();

                    output.write("!exit");
                    output.flush();

                    break;
                } else {
                    // 모든 클라이언트에게 전송
                    broadcast(clientId, clientId + ": " + line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 클라이언트 종료 시 ID 제거
            if (clientId != null) {
                clientWriters.remove(clientId); // 출력 스트림 제거
                System.out.println(clientId + " 연결 종료");
            }
        }
    }

    // 모든 클라이언트에게 메시지 전송 (자신 제외)
    private void broadcast(String senderId, String message) {
        Set<String> keys = clientWriters.keySet();

        for (String id : keys) {
            if (!id.equals(senderId)) { // 본인을 제외한 나머지 클라이언트에게만 메시지 전송
                try {
                    BufferedWriter writer = clientWriters.get(id);
                    writer.write(message);
                    writer.flush();
                } catch (IOException e) {
                    System.err.println("메시지 전송 중 오류: " + e.getMessage());
                }
            }
        }
    }

    // 특정 클라이언트에게 귓속말 전송
    private void sendWhisper(String fromId, String toId, String message) {
        BufferedWriter writer = clientWriters.get(toId);
        if (writer != null) {
            try {
                writer.write("[귓속말] " + fromId + ": " + message);
                writer.flush();
            } catch (IOException e) {
                System.err.println("귓속말 전송 중 오류: " + e.getMessage());
            }
        } else {
            try {
                // 수신자가 존재하지 않을 경우 보낸 사람에게 알림
                clientWriters.get(fromId).write("해당 ID가 존재하지 않습니다.\n");
                clientWriters.get(fromId).flush();
            } catch (IOException e) {
                System.err.println("메시지 전송 중 오류: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        int port = 1234;

        try {
            if (args.length > 1) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            System.err.println("잘못된 형식의 포트입니다." + args[0]);
            System.exit(1);
        }

        Server server = new Server(port);
        server.start();
    }
}
