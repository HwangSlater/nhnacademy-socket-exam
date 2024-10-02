package com.example.quiz13;

public class Quiz13 {
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

    }
}
