package com.monarchs.SkillBridge.utils;

import java.security.SecureRandom;

public class OtpGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateOtp(int length){
        String chars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder otpBuilder=new StringBuilder();
        for(int i=0;i<length;i++){
            otpBuilder.append(chars.charAt((secureRandom.nextInt(chars.length()))));
        }
        return otpBuilder.toString();
    }
}
