package com.chatapp.backend.javaMail;

/**
 *
 * @author hinas
 */
import java.util.Properties;

import javax.mail.Authenticator;

import javax.mail.Message;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

@Component
public class SendMail {

    public boolean sendEmail(String UserEmail, String subject, String messageToSent) {
        boolean test = false;
        String toEmail = UserEmail;
        final String fromEmail = "temporary300abc@gmail.com";
        final String password = "temporary123Account";
        try {
            Properties pr = new Properties();
            pr.setProperty("mail.smtp.host", "smtp.gmail.com");
            pr.setProperty("mail.smtp.port", "587");
            pr.setProperty("mail.smtp.auth", "true");

            //pr.put("mail.smtp.ssl.enable","true");
            pr.setProperty("mail.smtp.starttls.enable", "true");
            pr.put("mail.smtp.socketfactory.port", "587");
            pr.put("mail.smtp.socketfactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getInstance(pr, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(fromEmail, password);
                }

            });

            Message msg = new MimeMessage(session);

            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setSubject(subject);
            msg.setText(messageToSent);

            Transport.send(msg);
            test = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return test;

        }

    }
}
