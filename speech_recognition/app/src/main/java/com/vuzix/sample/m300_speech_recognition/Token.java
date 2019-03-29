package com.vuzix.sample.m300_speech_recognition;

public class Token {
    static String token;
    public static void setToken(String newToken)
    {
        token = newToken;
    }
    public static String getToken()
    {
        return token;
    }
}
