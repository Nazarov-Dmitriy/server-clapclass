package ru.clapClass.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clapClass.domain.dto.auth.AuthenticationResponse;
import ru.clapClass.domain.dto.auth.UserRequest;
import ru.clapClass.service.user.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/info")
    public AuthenticationResponse updateUserInfo(@RequestBody @Validated(UserRequest.UserInfo.class) UserRequest request) {
        return userService.updateUserInfo(request);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Validated(UserRequest.UserChangePassword.class) UserRequest req, HttpServletRequest request) {
        return userService.changePassword(req, request);
    }

    @PostMapping("/for-got-password")
    public ResponseEntity<?> forGotPassword(@RequestBody @Validated(UserRequest.ForGotPassword.class) UserRequest req) {
        return userService.forGotPassword(req);
    }

    @PostMapping("/avatar/add")
    public ResponseEntity<?> userAvatarAdd(@ModelAttribute UserRequest req) {
        return userService.addAvatar(req);
    }

    @GetMapping("/avatar/remove")
    public ResponseEntity<?> userAvatarRemove(HttpServletRequest request) {
        return userService.removeAvatar(request);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> userSubscribe(@RequestBody @Validated(UserRequest.Subscribe.class) UserRequest req) {
        return userService.subscribe(req);
    }
}


