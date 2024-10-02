package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

// Quiz-07. 프로그램에서 보내고 받는 과정에 별도로 동작하도록 구성하라.
public class Quiz07 {
    // main스레드와 receiver스레드 두개가 동작함
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
            System.err.println("잘못된 형식의 포트입니다.");
            System.exit(1);
        }

        try (Socket socket = new Socket(host, port);
                BufferedReader terminalIn = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));) {
            System.out.println("서버에 연결되었습니다.");

            // 서버에서의 입력을 받고 터미널에 출력하기 위한 스레드 생성
            Receiver receiver = new Receiver(input);
            receiver.start();

            // 클라이언트가 데이터를 입력하고 서버로 출력하게 함.
            String line;
            while ((line = terminalIn.readLine()) != null) {
                if (line.trim().equals("exit")) {
                    receiver.interrupt();
                    break;
                }

                output.write(line + "\n");
                output.flush();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

// 서버에서 보낸 데이터를 터미널(클라이언트)로 출력
class Receiver extends Thread {
    final BufferedReader input;

    public Receiver(BufferedReader input) {
        this.input = input;
    }

    @Override
    public void run() {
        char[] buffer = new char[2048];

        try {
            while (!Thread.currentThread().isInterrupted()) {
                int length = input.read(buffer);
                System.out.println(new String(buffer, 0, length).trim());
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
