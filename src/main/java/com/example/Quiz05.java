package com.example;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

// Quiz-05. Client socket을 server에 연결하여 server에서 보내오는 문자열을 출력하라.
public class Quiz05 {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1234;

        if (args.length > 0) {
            host = args[0];
        }

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

            // 소켓에서 입력 스트림을 가져옴 (서버에서 입력한 데이터를 가져올 예정)
            BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
            int readLength;
            byte[] buffer = new byte[2048];

            // inpurt.read로 데이터를 읽어와 buffer에 저장
            while ((readLength = input.read(buffer)) > 0) {
                // 저장된 버퍼를 0부터 readLength까지(저장된 길이만큼) 불러옴.
                // 그 후 trim을 사용하여 공백을 제거한 후 receiveData에 저장
                String receiveData = new String(buffer, 0, readLength).trim();
                if (receiveData.equals("exit")) {
                    break;
                }

                System.out.println(receiveData);
            }

            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
