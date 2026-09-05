package com.monarchs.SkillBridge.utils;

public class EmailBuilder {

    public static String otpEmailTemplateBuilder(String name, String otp){
        return String.format(""" 
                <html> 
                <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;"> 
                <div style="max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px;"> 
                <h1 style="color: #2563eb; text-align: center;"> SkillBridge </h1> 
                <h2 style="color: #333;"> Verify Your Email </h2>
                <p> Hi <strong>%s</strong>, </p> <p> Your OTP verification code is: </p> 
                <h2 style=" background: #f0f4ff; padding: 20px; text-align: center; color: #2563eb; letter-spacing: 5px; "> %s </h2> 
                <p style="color: #666;"> This code is valid for 10 minutes. </p> 
                <p style="color: #666;"> Please do not share this OTP with anyone. </p> 
                <hr style="border: none; border-top: 1px solid #eee; margin: 25px 0;"> 
                <p style="color: #999; font-size: 12px; text-align: center;"> © 2026 SkillBridge. All Rights Reserved. </p> 
                </div> 
                </body> 
                </html> """, name, otp);
    }

    public static String welcomeEmailBuilder(String name){
        return String.format(""" 
                <html> 
                <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;"> 
                <div style="max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px;"> 
                <h1 style="color: #2563eb; text-align: center;"> SkillBridge </h1> 
                <h2 style="color: #333; text-align: center;"> Welcome to SkillBridge! </h2> 
                <p> Hi <strong>%s</strong>, </p> 
                <p> Thank you for joining <strong>SkillBridge</strong>. Your account has been successfully created. </p> 
                <div style=" background-color: #f0f4ff; border-left: 4px solid #2563eb; padding: 15px; margin: 20px 0; "> 
                <p style="margin: 0;"> We're excited to have you on SkillBridge. Complete your profile and start discovering opportunities that match your skills. </p> 
                </div> <p> Best wishes,<br> <strong>The SkillBridge Team</strong> </p> <hr style="border: none; border-top: 1px solid #eee; margin: 25px 0;"> 
                <p style="color: #999; font-size: 12px; text-align: center;"> © 2026 SkillBridge. All Rights Reserved. </p> </div> 
                </body> 
                </html> """, name);
    }
}
