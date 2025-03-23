package ru.clapClass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.domain.dto.email.FormRequest;
import ru.clapClass.domain.dto.email.OfferMaterialRequest;
import ru.clapClass.domain.dto.email.SendThemeRequest;
import ru.clapClass.service.mail.EmailService;

import java.util.List;

@RestController
@RequestMapping("send-mail")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/faq")
    public ResponseEntity<?> sendFaq(@RequestBody @Validated FormRequest req) {
        return emailService.sendFaq(req);
    }

    @PostMapping(path = "/offer-material")
    public ResponseEntity<?> offerMaterial(@Validated OfferMaterialRequest req, List<MultipartFile> files) {
        return emailService.offerMaterial(req, files);
    }

    @PostMapping(path = "/theme")
    public ResponseEntity<?> sendTheme(@RequestBody @Validated SendThemeRequest req) {
        return emailService.sendTheme(req);
    }
}
