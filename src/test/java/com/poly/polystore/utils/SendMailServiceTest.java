package com.poly.polystore.utils;

import com.poly.polystore.dto.MailInfo;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SendMailServiceTest {
    @Autowired

    private SendMailService sendMailService;

    @Test
    void send() {
        try {
            sendMailService.send("tranquangvinh4793@gmail.com","Test","Test subject",null);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}