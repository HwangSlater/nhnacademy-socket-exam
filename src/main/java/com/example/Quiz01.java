package com.example;

import java.net.Socket;

// Quiz-01. Server에 연결 가능한 port가 무엇이 있는지 검색하는 프로그램을 만들자.
public class Quiz01 {
    public static void main(String[] args) {
        int startPort = 0;
        int endPort = 65535;

        for (int port = startPort; port < endPort; port++) {
            try {
                Socket socket = new Socket("localhost", port);
                System.out.println("Port " + port + " 열려 있습니다");

                socket.close();
            } catch (Exception e) {
            }
        }
    }
}
