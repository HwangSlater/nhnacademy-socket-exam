package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

// Quiz-09. Echo Server를 만들어 보자.
public class Quiz09 {
    public static void main(String[] args) {
        int port = 1234;

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            System.err.println("잘못된 형식의 포트입니다.");
            System.exit(1);
        }

        try (ServerSocket server = new ServerSocket(port)) {
            Socket socket = server.accept();
            System.out.println("Client[" + socket.getInetAddress().getHostAddress() +
                    ":" + socket.getPort() + "]가 연결되었습니다.");

            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line;
            while ((line = socketIn.readLine()) != null) {
                socketOut.write(line.trim() + "\n");
                socketOut.flush();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}
