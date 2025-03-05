package ru.clapClass.service.mail;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.domain.dto.email.ContactUsRequest;
import ru.clapClass.domain.dto.email.FormRequest;
import ru.clapClass.domain.dto.email.OfferMaterialRequest;
import ru.clapClass.domain.models.article.ArticleModel;
import ru.clapClass.domain.models.user.User;
import ru.clapClass.exception.BadRequest;
import ru.clapClass.exception.InternalServerError;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private Configuration configuration;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${url-backend}")
    private String urlBackend;

    @Value("${url-frontend}")
    private String urlFrontend;

    public void sendSimpleEmail(Map<String, String> params, MultipartFile file) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(params.get("subject"));
            helper.setFrom(username);
            helper.setTo(params.get("email"));
            String emailContent = sendEmailWithContent(params);
            helper.setText(emailContent, true);
            if (file != null) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }

            emailSender.send(mimeMessage);
        } catch (MailSendException | IOException | TemplateException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendEmailWithContent(Map<String, String> params) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate(params.get("template")).process(params, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    @Async
    public void sendMessageRegisterUser(User user, String password) {
        try {
            var url = urlBackend + "images/logo.png";
            Map<String, String> params = new HashMap<>();
            params.put("email", user.getEmail());
            params.put("subject", "Регистрация ClapClass");
            params.put("password", password);
            params.put("link_img", url);
            params.put("template", "register.ftlh");
            params.put("link_sait", urlFrontend.split(",")[0]);
            sendSimpleEmail(params, null);
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public void sendForGotPassword(String password, String email) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("subject", "Восстановление пароля");
            params.put("password", password);
            params.put("template", "for-got-password.ftlh");
            sendSimpleEmail(params, null);
            ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public ResponseEntity<?> sendFaq(FormRequest req) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("email", username);
            params.put("subject", "Вопросы и предложения");
            params.put("name", req.name());
            params.put("phone", req.phone());
            params.put("email_user", req.email());
            params.put("question", req.question());
            params.put("template", "faq.ftlh");
            sendSimpleEmail(params, null);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new BadRequest("ошибка данных", "error");
        }
    }

    public ResponseEntity<?> offerMaterial(OfferMaterialRequest req, MultipartFile file) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("email", username);
            params.put("subject", "Предложить кейс");
            params.put("name", req.getName());
            params.put("email_user", req.getEmail());
            params.put("title", req.getTitle());
            params.put("type", req.getType());
            params.put("template", "offer_material.ftlh");
            sendSimpleEmail(params, file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new BadRequest("ошибка данных", "error");
        }
    }

    @Async
    public void sendMessageMaterial(Optional<List<User>> subscriberUsers, String pathMaterial, ArticleModel article) {
        subscriberUsers.ifPresent(items -> {
                    for (var item : items) {
                        try {
                            var url = urlFrontend.split(",")[0] + pathMaterial;
                            Map<String, String> params = new HashMap<>();
                            params.put("email", item.getEmail());
                            params.put("subject", article.getTitle());
                            params.put("title", article.getTitle());
                            params.put("template", "material.ftlh");
                            params.put("link_sait", url);
                            sendSimpleEmail(params, null);
                        } catch (Exception e) {
                            throw new InternalServerError(e.getMessage());
                        }
                    }
                }
        );
    }

    public ResponseEntity<?> contactUs(ContactUsRequest req) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("email", username);
            params.put("subject", "Форма связаться с нами");
            params.put("name", req.name());
            params.put("email_user", req.email());
            params.put("phone", req.phone());
            params.put("theme", req.message());
            params.put("question", req.textarea());
            params.put("template", "contact_us.ftlh");
            sendSimpleEmail(params, null);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new BadRequest("ошибка данных", "error");
        }
    }
}