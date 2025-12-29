package ch.no1hardy.service.service;

import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.verification.VerificationCode;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {
    @Value("${spring.mail.host}")
    private String smtp;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${wm.host.name}")
    private String hostName;

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", port);

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        return Session.getInstance(props, auth);
    }

    private String formatAsHtml(String body) {
        return body
                .replace("\n", "<br>")
                .replace("ä", "&auml;")
                .replace("ö", "&ouml;")
                .replace("ü", "&uuml;")
                .replace("Ä", "&Auml;")
                .replace("Ö", "&Ouml;")
                .replace("Ü", "&Uuml;");
    }

    public void sendMail(String to, String subject, String body) {
        try {
            Message message = new MimeMessage(createSession());
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = formatAsHtml(body);
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Sent email '" + subject + "' to " + to);
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
        }
    }

    private String addSubHeading(String subHeading) {
        return "<h2>" + subHeading + "</h2>";
    }

    private String addParagraph(String paragraph) {
        return "<p>" + paragraph + "</p>";
    }

    private String addLink(String url, String text) {
        return "<a href=\"" + url + "\">" + text + "</a>";
    }

    public void sendEmailVerificationMail(User user, VerificationCode code) {
        String subject = "Bestätige deine Emailadresse";
        String link = hostName + "/verify-email?code=" + code.getCode();

        String body = addParagraph("Hallo <b>" + user.getUsername() + "</b>,") +
                addParagraph("Klicke auf den folgenden Link, um deine Emailadresse zu bestätigen:&nbsp;" + addLink(link, link)) +
                addSubHeading("Wie geht es weiter?") +
                addParagraph("Nachdem du deine Emailadresse best&auml;tigt hast, wirst du für die manuelle Freischaltung durch einen Administrator vorgemerkt.<br>" +
                        "Du wirst per Email benachrichtigt, sobald dein Account freigeschaltet wurde.<br>" +
                        "In der Zwischenzeit kannst du aber bereits unlimitiert deine <b>Wetten platzieren.</b>") +
                addParagraph("Liebe Gr&uuml;sse,<br><b>Dein WM-Team 2026</b>");
        sendMail(user.getEmail(), subject, body);
    }
}
