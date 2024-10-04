package com.example.quiz13;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/*
 * ClientHandler는 비동기적으로 통신을 처리하여 여러 클라이언트와 동시에 상호작용합니다.
 * 
 * 서버와 클라이언트 간의 연결을 관리하며
 * 클라이언트의 메시지를 수신하여 다른 클라이언트에게 전달합니다.
 */
class ClientHandler implements Runnable {
    Socket clientSocket;
    Server server;
    BufferedWriter output;
    String clientId;

    ClientHandler(Socket socket, Server server) {
        clientSocket = socket;
        this.server = server;
        try {
            output = new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            System.err.println("클라이언트 스트림 초기화 중 오류: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try (BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter terminalOut = new BufferedWriter(new OutputStreamWriter(System.out))) {

            clientId = socketIn.readLine().replace("가 접속했습니다.", "").trim();
            if (!server.idMap.containsKey(clientId)) {
                server.idMap.put(clientId, this);
            } else {
                output.write("이미 접속 중인 ID입니다.\n");
                output.flush();

                removeSocket();
            }

            String line;
            while ((line = socketIn.readLine()) != null) {
                if (line.trim().charAt(0) == '@') {
                    // id와 메시지 두개로 분리
                    String[] lines = line.trim().split(" ", 2);
                    if (lines.length == 2) {
                        // @를 제외한 id 추출
                        String targetId = lines[0].substring(1);
                        String message = lines[1];

                        if (server.idMap.containsKey(targetId)) {
                            ClientHandler targetClientHandler = server.idMap.get(targetId);
                            server.sendWhisper(message, targetClientHandler);
                        } else {
                            output.write("해당 ID가 존재하지 않습니다.");
                            output.flush();
                        }
                    } else {
                        output.write("올바른 형식이 아닙니다.");
                        output.flush();
                    }
                    // 제어 명령
                } else if (line.trim().charAt(0) == '!') {
                    String command = line.trim().substring(1);
                    if (command.equals("exit")) {
                        removeSocket();
                    } else {
                        output.write("해당하는 명령어가 존재하지 않습니다.");
                        output.flush();
                    }
                } else {
                    server.sendMessage(line.trim(), this);
                    terminalOut.write(line.trim() + "\n");
                    terminalOut.flush();
                }
            }

            removeSocket();
        } catch (IOException e) {
            System.err.println("클라이언트와의 통신 중 오류: " + e.getMessage());
        }
    }

    // 클라이언트와의 연결을 종료하고 관련 자원을 정리
    public void removeSocket() {
        try {
            output.write("exit");
            output.flush();

            server.removeClient(this);
            server.idMap.remove(clientId);
            clientSocket.close();
            output.close();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.err.println("소켓 닫는 도중 오류");
        }
    }

    public void receivedMessage(String message) {
        try {
            output.write(message + "\n");
            output.flush();
        } catch (IOException e) {
            System.err.println("메시지 전송 중 오류: " + e.getMessage());
        }
    }
}