package api.giybat.uz.api.giybat.uz.service;
import api.giybat.uz.api.giybat.uz.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailSendingService {

    @Value("${spring.mail.username}")
    private String fromAccount;

    @Value("${server.domain}")
    private String serverDomain;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailForRegistration(String email, Integer profileId){
        String subject = "Ro'yxatdan o'tish";
        String body = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "    <style>\n" +
                "        a{\n" +
                "            padding: 10px 30px;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "        .tugma{\n" +
                "          text-decoration: none;\n" +
                "            color: darkslategrey;\n" +
                "            background-color: wheat;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "        .tugma:hover{\n" +
                "            color: white;\n" +
                "            background-color: darkgray;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Ro'yxatdan o'tish</h1>\n" +
                "<p>Ro'yxatdan o'tishni yakunlash uchun tugmani bosing:\n" +
                "    <a  class=\"tugma\"\n style=`hover:  color: white;background-color: darkgray;`" +
                "            href=\"%s/auth/registration/verification/%s\" target=\"_blank\">tasdiqlash</a></p>\n" +
                "</body>\n" +
                "</html>";

        body = String.format(body,serverDomain, JwtUtil.encode(profileId));
        sendMimeEmail(email,subject,body);
    }



    private void sendMimeEmail(String email, String subject, String body){

        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            msg.setFrom(fromAccount);

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);

            // threadga olish
            CompletableFuture.runAsync(() ->{
                javaMailSender.send(msg);
            });
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

    // 1-usul oddiy text formatida xabar yuborish
    private void sendOddiyTextEmail(String email, String subject, String body){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAccount);
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(body);
        javaMailSender.send(msg);

    }

}
