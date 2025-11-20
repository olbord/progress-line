package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.exception.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import jakarta.transaction.*;
import lombok.*;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendMail(String recipient, String senderEmail, String subject, String body) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("noreply@jairus.ee");

            helper.setTo(recipient);

            helper.setReplyTo(senderEmail);

            helper.setSubject(subject);
            helper.setText(body, false);

        } catch (MessagingException e) {
            throw new ApplicationException("Meili seadistamise viga: " + e.getMessage());
        }

        try {
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new ApplicationException("Meili saatmine ebaõnnestus: " + e.getMessage());
        }
    }

}