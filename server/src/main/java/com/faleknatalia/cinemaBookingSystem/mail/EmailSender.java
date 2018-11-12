package com.faleknatalia.cinemaBookingSystem.mail;

import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class EmailSender {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String title, String content, ByteArrayOutputStream doc) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content, true);
            InputStream is = new ByteArrayInputStream(doc.toByteArray());
            helper.addAttachment("ticket.pdf", new ByteArrayResource(IOUtils.toByteArray(is)));
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mail);
    }
}

