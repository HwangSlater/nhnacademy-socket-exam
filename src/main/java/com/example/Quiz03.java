package com.example;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

// Quiz-03. Socket class의 함수를 이용해 client와 server 접속 정보(host, port)를 확인하자
public class Quiz03 {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1234;

        // 로컬 머신은 현재 코드가 실행되고 있는 컴퓨터를 의미 (서버와 클라이언트 둘 중 관점에 따라 로컬머신은 달라짐)
        try {
            Socket socket = new Socket(host, port);
            System.out.println("서버에 연결되었습니다.");

            // getLocalAddress : 소켓이 로컬 머신에서 사용하고 있는 IP주소 반환 (vscode의 IP주소)
            // getHostAddress : 현재 소켓의 로컬 주소 가져옴 (vscode의 주소)
            System.out.println("Local address: " + socket.getLocalAddress().getHostAddress());
            // getLocalPort : 현재 소켓의 로컬 포트 가져옴 (vscode의 포트)
            System.out.println("Local port: " + socket.getLocalPort());
            // getInetAddress : 소켓이 연결된 원격 머신의 IP주소를 반환 (터미널의 IP주소)
            System.out.println("Remote address: " + socket.getInetAddress().getHostAddress());
            // getPort : 현재 소켓의 원격 포트를 가져옴 (1234로 연결 하면 1234가 됨)
            System.out.println("Remote port: " + socket.getPort());

            socket.close();
        } catch (ConnectException e) {
            System.err.println(host + ": " + port + "에 연결할 수 없습니다.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
