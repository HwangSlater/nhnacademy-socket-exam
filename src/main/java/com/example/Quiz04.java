package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

// Quiz-04. Client socket을 server에 연결하여 data를 보내고, exit가 입력되면 프로그램을 종료하라.
public class Quiz04 {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1234;

        try {
            Socket socket = new Socket(host, port);
            System.out.println("서버에 연결되었습니다.");

            // 데이터 입출력을 위한 stream을 얻음
            OutputStream output = socket.getOutputStream();
            // 전송할 데이터는 표준 입력을 이용
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                // 줄 단위로 받음
                String line = bufferedReader.readLine();
                // exit를 입력하면 종료
                if (line.equals("exit")) {
                    break;
                }

                // 서버에 보내기 위해 byte[]형태로 버퍼에 저장
                output.write((line + "\n").getBytes());
                // 서버로 보냄
                output.flush();
            }

            socket.close();
        } catch (ConnectException e) {
            System.err.println(host + " : " + port + "에 연결할 수 없습니다.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
