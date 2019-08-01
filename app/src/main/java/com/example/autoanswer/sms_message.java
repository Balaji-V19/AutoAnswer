package com.example.autoanswer;

public class sms_message {
    public static String message;
    public static int sec;

    public static int getSec() {
        return sec;
    }

    public static void setSec(int sec) {
        sms_message.sec = sec;
    }

    public static String getMessage() {
        return message;
    }

    public static void setMessage(String message) {
        sms_message.message = message;
    }

    @Override
    public String toString() {
        return "sms_message{}";
    }
}
