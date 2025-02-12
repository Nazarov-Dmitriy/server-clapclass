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
import ru.clapClass.servise.mail.EmailService;

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

    public ResponseEntity<?> offerMaterial(@Validated OfferMaterialRequest req, MultipartFile file) {
        return emailService.offerMaterial(req, file);
    }
}
