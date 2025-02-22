package ru.clapClass.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clapClass.domain.dto.auth.AuthenticationResponse;
import ru.clapClass.domain.dto.auth.UserRequest;
import ru.clapClass.domain.dto.auth.UserResponse;
import ru.clapClass.domain.mapper.UserMapper;
import ru.clapClass.domain.models.user.Role;
import ru.clapClass.domain.models.user.User;
import ru.clapClass.exception.BadRequest;
import ru.clapClass.exception.InternalServerError;
import ru.clapClass.repository.UserRepository;
import ru.clapClass.security.CustomerUserDetailsService;
import ru.clapClass.service.mail.EmailService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Autowired
    EmailService emailService;


    public ResponseEntity<?> signUp(UserRequest request) {
        try {
            var user = User.builder().email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.user).build();
            userService.create(user);
            emailService.sendMessageRegisterUser(user, request.getPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            if (e instanceof BadRequest) {
                throw new BadRequest("Пользователь с таким email уже существует", "email");
            }
            throw new InternalServerError(e.getMessage());
        }
    }

    public AuthenticationResponse signIn(UserRequest request) {
        var user = customerUserDetailsService.loadUserByUsername(request.getEmail());

        if (user == null) {
            throw new BadRequest("Пользователь не найден", "user");
        }
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new BadRequest("не правильный пароль", "password");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var jwt = jwtService.generateToken(user, request.getTime_token());
        var responseUser = userMapper.toUserResponse(user.getUser());
        return new AuthenticationResponse(jwt, responseUser);
    }

    public UserResponse autoLogin(UserRequest token) {
        var email = jwtService.extractUserName(token.getToken());
        var user = userRepository.findByEmail(email);
        return userMapper.toUserResponse(user.get());
    }
}



