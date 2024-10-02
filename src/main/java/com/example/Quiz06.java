package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

// Quiz-06. Echo server에 연결하여 문자열을 보내고 받도록 구성하라.
public class Quiz06 {
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
            System.err.println("문자가 아닙니다.");
            System.exit(1);
        }

        /*
         * 클라이언트가 서버로 데이터를 보냄.
         * 서버는 데이터를 받고 클라이언트에게 데이터를 보냄.
         * 서버에서 데이터를 받기 위해 대기하고 있던 클라이언트는 받은 데이터를 터미널로 다시 출력함.
         */
        try (Socket socket = new Socket(host, port);
                // 서버에서 입력 받은 데이터를 스트림 버퍼에 저장
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // 터미널에서 입력 받은 데이터를 서버로 출력하기 위한 스트림
                BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                // 터미널에서 입력 받은 스트림을 버퍼에 저장
                BufferedReader terminalIn = new BufferedReader(new InputStreamReader(System.in));
                // 서버에서 입력 받은 데이터를 클라이언트에게 출력하기 위한 스트림
                BufferedWriter terminalOut = new BufferedWriter(new OutputStreamWriter(System.out));) {
            System.out.println("서버에 연결되었습니다.");

            String line;

            // 터미널에서 입력 받아서 line에 저장
            while ((line = terminalIn.readLine()) != null) {
                // 서버로 전송
                socketOut.write(line + "\n");
                socketOut.flush();

                // 서버의 입력을 받기 까지 계속 대기(입력을 받아야 while문이 다시 돔)
                line = socketIn.readLine();
                // 입력받은 line을 터미널(클라이언트)에 출력
                terminalOut.write(line + "\n");
                terminalOut.flush();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}