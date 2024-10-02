package com.example;

import java.io.IOException;
import java.net.Socket;

// Quiz-02. try-with-resources를 이용해 socket 자원을 관리하라.
public class Quiz02 {
    public static void main(String[] args) {
        int startPort = 0;
        int endPort = 65535;

        for (int port = startPort; port < endPort; port++) {
            try (Socket socket = new Socket("localhost", port)) {
                System.out.println(port + "에 연결되었습니다.");
            } catch (IOException e) {
                // 연결이 거부되면 IOException이 일어나므로
                // 연결된거만 보려면 err지워야함.
                System.err.println(e);
            }
        }

    }
}
