package ru.clapClass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clapClass.domain.dto.auth.AuthenticationResponse;
import ru.clapClass.domain.dto.auth.UserRequest;
import ru.clapClass.domain.dto.auth.UserResponse;
import ru.clapClass.servise.user.AuthenticationService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Validated(UserRequest.SignUp.class) UserRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public AuthenticationResponse signIn(@RequestBody @Validated(UserRequest.SignIn.class) UserRequest request) {
        return authenticationService.signIn(request);
    }

    @PostMapping("/auto-login")
    public UserResponse autoLogin(@RequestBody UserRequest token) {
        return authenticationService.autoLogin(token);
    }
}


