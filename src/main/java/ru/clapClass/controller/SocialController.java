package ru.clapClass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clapClass.domain.dto.social.SocialRequest;
import ru.clapClass.service.social.SocialService;

@RestController
@RequestMapping("social/")
@RequiredArgsConstructor
public class SocialController {
    private final SocialService socialService;

    @PostMapping(path = "/add")
    public ResponseEntity<?> addSocial(@RequestBody @Validated SocialRequest socialRequest) {
        return socialService.addSocial(socialRequest);
    }

    @GetMapping(path = "/list")
    public ResponseEntity<?> getList() {
        return socialService.getList();
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<?> editReview(@RequestBody @Validated SocialRequest socialRequest) {
        return socialService.editSocial(socialRequest);
    }
    
    @GetMapping("/remove/{name}")
    public ResponseEntity<?> remove(@PathVariable String name) {
        return socialService.remove(name);
    }
}
