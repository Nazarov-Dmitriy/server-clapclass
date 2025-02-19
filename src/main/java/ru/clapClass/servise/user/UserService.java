package ru.clapClass.servise.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clapClass.domain.dto.auth.AuthenticationResponse;
import ru.clapClass.domain.dto.auth.UserRequest;
import ru.clapClass.domain.mapper.UserMapper;
import ru.clapClass.domain.models.user.User;
import ru.clapClass.exception.BadRequest;
import ru.clapClass.exception.InternalServerError;
import ru.clapClass.repository.UserRepository;
import ru.clapClass.security.JwtUser;
import ru.clapClass.servise.mail.EmailService;
import ru.clapClass.servise.s3.ServiceS3;
import ru.clapClass.utils.FileCreate;
import ru.clapClass.utils.HeaderToken;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ServiceS3 serviceS3;

    @Autowired
    EmailService emailService;

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new BadRequest("Пользователь с таким email уже существует", "email");
        }
        return save(user);
    }

    public AuthenticationResponse updateUserInfo(UserRequest request) {
        if (!request.getEmail().equals(request.getCurrent_email())) {
            if (repository.existsByEmail(request.getEmail())) {
                throw new BadRequest("Пользователь с таким email уже существует", "email");
            }
        }
        String jwt = "";

        Optional<User> user = repository.findByEmail(request.getCurrent_email());
        if (user.isPresent()) {
            userMapper.updateUserFromDto(request, user.get());
            user.get().setCompleted_profile(true);
            repository.save(user.get());
            var responseUser = userMapper.toUserResponse(user.get());

            if (!user.get().getEmail().equals(request.getCurrent_email())) {
                var jwtUser = new JwtUser(user.get());
                jwt = jwtService.generateToken(jwtUser, request.getTime_token());
            }

            return new AuthenticationResponse(jwt, responseUser);
        } else {
            throw new BadRequest("ошибка данных", "error");
        }
    }

    public ResponseEntity<String> changePassword(UserRequest req, HttpServletRequest request) {
        var token = HeaderToken.getToken(request);
        var email = jwtService.extractUserName(token);
        Optional<User> user = repository.findByEmail(email);

        if (user.isPresent()) {
            if (!BCrypt.checkpw(req.getPassword(), user.get().getPassword())) {
                throw new BadRequest("не правильный пароль", "password");
            } else {
                user.get().setPassword(passwordEncoder.encode(req.getNew_password()));
                repository.save(user.get());

                var jwtUser = new JwtUser(user.get());
                var jwt = jwtService.generateToken(jwtUser, req.getTime_token());
                return new ResponseEntity<>(jwt, HttpStatus.OK);
            }
        } else {
            throw new InternalServerError("ошибка данных", "error");
        }
    }

    public ResponseEntity<?> forGotPassword(UserRequest req) {
        try {
            String password = new Random().ints(10, 33, 122).collect(StringBuilder::new,
                            StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            var passwordHash = passwordEncoder.encode(password);
            var userGet = userRepository.findByEmail(req.getEmail());
            if (userGet.isPresent()) {
                userGet.get().setPassword(passwordHash);
                userRepository.save(userGet.get());

                if (passwordHash.equals(userGet.get().getPassword())) {
                    emailService.sendForGotPassword(password, req.getEmail());
                } else {
                    throw new InternalServerError("Ошибка смены пароля");
                }
            }
            return new ResponseEntity<>(password, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addAvatar(UserRequest req) {
        try {
            Optional<User> user = repository.findByEmail(req.getEmail());
            if (user.isPresent() ) {
                var path = "avatar/" + user.get().getId() + "/" + req.getAvatar().getOriginalFilename();
                if (user.get().getAvatar() != null) {
                    serviceS3.deleteObject(path);
                }
                serviceS3.putObject(path, req.getAvatar());
                var file = FileCreate.addFileS3(req.getAvatar(), new StringBuilder(path));
                user.get().setAvatar(file);
                repository.save(user.get());
                assert file != null;
                return new ResponseEntity<>(file.getPath(), HttpStatus.OK);
            }
            throw new BadRequest("ошибка данных", "errors");
        } catch (BadRequest e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> removeAvatar(HttpServletRequest request) {
        var token = HeaderToken.getToken(request);
        var email = jwtService.extractUserName(token);
        Optional<User> user = repository.findByEmail(email);
        if (user.isPresent()) {
            var pathFile = user.get().getAvatar().getPath();
            serviceS3.deleteObject(pathFile);
            user.get().setAvatar(null);
            repository.save(user.get());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> subscribe(UserRequest req) {
        try {
            var user = userRepository.findByEmail(req.getEmail());
            if (user.isPresent()) {
                if (!user.get().getSubscribe()) {
                    user.get().setSubscribe(true);
                    userRepository.save(user.get());
                }
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                throw new BadRequest("Пользователь не найден", "error");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
