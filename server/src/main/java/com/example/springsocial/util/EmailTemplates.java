package com.example.springsocial.util;

import org.springframework.mail.SimpleMailMessage;

public class EmailTemplates {
    
     public static SimpleMailMessage newUserEmail(String userName,String email,String userVerificationCode){
    SimpleMailMessage mailMessage = new SimpleMailMessage();

     mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom(Constants.NETWORK_EMAIL);
        mailMessage.setText("Dear "+userName+",\n\n" +
            "To confirm your account, please click here : "
            +"http://localhost:8080/confirm-account?verify="+userVerificationCode +
            "\n\n" +
            "Best regards,\n" +
            Constants.NETWORK_NAME);
            return mailMessage;
  }


  public static SimpleMailMessage passwordRessetEmail(String userName,String email,String userVerificationCode){
    SimpleMailMessage mailMessage = new SimpleMailMessage();
     mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset!");
        mailMessage.setFrom(Constants.NETWORK_EMAIL);
            mailMessage.setText("Dear "+userName+",\n\n" +
            "You've requested a password reset for your account. To reset your password, please click the link below:\n\n" +
            "http://localhost:8080/api/v1/public/password/verify?code="+ userVerificationCode +"\n\n"+
            "If you did not initiate this request, please disregard this email.\n\n" +
            "Best regards,\n" +
            Constants.NETWORK_NAME);
            return mailMessage;
  }

    public static SimpleMailMessage postLikedOrCommentedEmail(String email,String userName,String postLink,String postTitle){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("إشعار جديد على منشورك!");
        mailMessage.setFrom(Constants.NETWORK_EMAIL);
        mailMessage.setText("مرحبا " + userName + ",\n\n" +
                "لقد تمت إضافة تعليق أو إعجاب جديد على منشورك.\n" +
                "عنوان المنشور: " + postTitle + "\n" +
                "رابط المنشور: " + postLink + "\n\n" +
                "إذا كنت ترغب في الاطلاع على التعليق أو الإعجاب، يرجى زيارة الرابط أدناه:\n\n" +
                "http://localhost:8080/api/v1/public/post/" + postLink + "\n\n" +
                "إذا لم تكن أنت من قام بهذا الإجراء، يرجى تجاهل هذا البريد الإلكتروني.\n\n" +
                "أطيب التحيات،\n" +
                Constants.NETWORK_NAME);
        return mailMessage;

    }



}