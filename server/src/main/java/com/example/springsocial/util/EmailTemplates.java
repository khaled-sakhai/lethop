package com.example.springsocial.util;

import com.example.springsocial.entity.postRelated.Post;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

public class EmailTemplates {
    
     public static SimpleMailMessage newUserEmail(String userName,String email,String userVerificationCode){
    SimpleMailMessage mailMessage = new SimpleMailMessage();

     mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setSubject("تفعيل حسابك على موقعنا");
        mailMessage.setFrom(Constants.NETWORK_EMAIL);
        mailMessage.setText("مرحباً " + userName + ",\n\n" +

             "شكراً لتسجيلك في موقعنا!\n" +
            "لتفعيل حسابك، يرجى النقر على الرابط التالي:\n\n" +
            PathConstants.PRODUCTION_URL+"/auth/email/confirm?verify="+userVerificationCode +
             "\n\n" +
            "إذا كنت لم تقم بتسجيل في موقعنا، يرجى تجاهل هذا البريد الإلكتروني.\n\n" +
            "شكراً لك،\n" +
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


    public static SimpleMailMessage accountActivationReminderEmail(String email, String userName, String userVerificationCode) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("تذكير بتفعيل حسابك");
        mailMessage.setFrom(Constants.NETWORK_EMAIL);
        mailMessage.setText("مرحباً " + userName + ",\n\n" +
                "هذا تذكير بأن حسابك على موقعنا لم يتم تفعيله بعد.\n" +
           
                "لتفعيل حسابك، يرجى النقر على الرابط التالي:\n\n" +
                "http://localhost:8080/confirm-account?verify="+userVerificationCode +

                "إذا قمت بتفعيل حسابك بالفعل، يرجى تجاهل هذا البريد الإلكتروني.\n\n" +
                "شكراً لك،\n" +
                Constants.NETWORK_NAME);
        return mailMessage;
    }


    public static SimpleMailMessage weeklyPostsEmail(String userName, String email, List<Post> posts) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject("أفضل 10 مقالات هذا الأسبوع!");
        mailMessage.setFrom(Constants.NETWORK_EMAIL);

        StringBuilder text = new StringBuilder();
        text.append("مرحباً ").append(userName).append(",\n\n");
        text.append("إليك أفضل 10 مقالات لهذا الأسبوع:\n\n");

        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            text.append("المقال ").append(i + 1).append(":\n");
            text.append("العنوان: ").append(post.getTitle()).append("\n");
            int maxLength = Math.min(post.getContent().length(), 240);
            text.append("الوصف: ").append(post.getContent().substring(maxLength)).append("\n");
            text.append("الرابط: ").append(Constants.NETWORK_URL).append(post.getId()).append("\n\n");
        }

        text.append("شكراً لك،\n");
        text.append(Constants.NETWORK_NAME);

        mailMessage.setText(text.toString());

        return mailMessage;
    }

}
