package com.project.jeeproject_springboot.util;

import com.project.jeeproject_springboot.service.UserService;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;
import java.util.regex.Pattern;

public class EmailUtil {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String SMTP_USERNAME = "ewen.dump@gmail.com";
    private static final String SMTP_PASSWORD = "mzop alcl jtlv ncxj";

    public static void sendEmail(String to, String subject, String messageContent) throws MessagingException {
        // Configurer les propriétés du serveur SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        // Créer une session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        // Créer un message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SMTP_USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(messageContent);

        // Envoyer le message
        Transport.send(message);
    }

    public static boolean validEmail(String email) {
        // check not empty email
        if (email == null) {return false;}
        // check unique email
        // TODO
        // check email pattern
        Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        return EMAIL_PATTERN.matcher(email).matches();
    }
}

