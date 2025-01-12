package com.example.formPatient;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class JavaMailAPI {
    private String userEmail = "abirzitoun336@gmail.com";
    private String userPassword = "wezr hgcg uclk anov";
    private String recipientEmail;
    private String subject;
    private String messageBody;

    public JavaMailAPI(String recipientEmail, String subject, String messageBody) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.messageBody = messageBody;
    }

    public void sendEmail() {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.connectiontimeout", "5000");
        properties.put("mail.smtp.timeout", "5000");
        properties.put("mail.smtp.writetimeout", "5000");


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userEmail, userPassword);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // Recipient email
            message.setSubject(subject);
            message.setText(messageBody);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Transport.send(message);
                        System.out.println("Email sent successfully!");
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
