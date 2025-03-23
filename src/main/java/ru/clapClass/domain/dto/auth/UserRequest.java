package ru.clapClass.domain.dto.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.domain.models.user.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов", groups = {SignUp.class, SignIn.class, Subscribe.class, ForGotPassword.class})
    @NotBlank(message = "Поле не должно быть пустым", groups = {SignUp.class, SignIn.class, Subscribe.class, ForGotPassword.class})
    @Email(message = "Email адрес должен быть в формате user@example.ru", groups = {SignUp.class, UserInfo.class, Subscribe.class, SignIn.class, ForGotPassword.class})
    private String email;

    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 символов", groups = {SignUp.class, SignIn.class, UserChangePassword.class})
    @NotBlank(message = "Пароль не может быть пустыми", groups = {SignUp.class, SignIn.class, UserChangePassword.class})
    private String password;

    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 символов", groups = {UserChangePassword.class})
    @NotBlank(message = "Поле не должно быть пустым", groups = {UserChangePassword.class})
    private String new_password;

    @NotEmpty(message = "Поле не должно быть пустым", groups = {UserInfo.class})
    private String name;

    @NotEmpty(message = "Поле не должно быть пустым", groups = {SignIn.class})
    private String time_token;

    @NotEmpty(message = "Поле не должно быть пустым", groups = {UserInfo.class})
    private String phone;

    @NotEmpty(message = "Поле не должно быть пустым", groups = {UserInfo.class})
    private String place_work;

    @NotEmpty(message = "Поле не должно быть пустым", groups = {UserInfo.class})
    private String position;

    @NotEmpty(message = "Поле не должно быть пустым", groups = {UserInfo.class})
    private String city;

    private String token;

    private String current_email;

    private MultipartFile avatar;

    @NotNull(message = "Поле не должно быть пустым", groups = {ChangeRole.class})
    private Role role;

    @NotNull(message = "Поле не должно быть пустым", groups = {ChangeRole.class})
    private Long user_id;

    /**
     * Группа проверок для регистрации
     */
    public interface SignUp {
    }

    /**
     * Группа проверок для авторизации
     */
    public interface SignIn {
    }

    /**
     * Группа проверок для регистрации информации о пользователи
     */
    public interface UserInfo {
    }

    /**
     * Группа проверок для смены пароля
     */
    public interface UserChangePassword {
    }

    /**
     * Группа проверок для восстановления пароля
     */
    public interface ForGotPassword {
    }

    /**
     * Группа проверок для восстановления пароля
     */
    public interface Subscribe {
    }

    /**
     * Группа проверок для восстановления пароля
     */
    public interface ChangeRole {
    }
}



