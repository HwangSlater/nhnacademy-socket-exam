package com.example.exam;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

// Exam-01. Client socket을 이용해 server에 연결해 보자.
public class Exam01 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            System.out.println("서버에 연결되었습니다");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}