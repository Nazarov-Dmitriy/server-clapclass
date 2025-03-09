package ru.clapClass.domain.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


public record SendThemeRequest(@NotEmpty(message = "Поле не должно быть пустым") String name,
                               @NotEmpty(message = "Поле не должно быть пустым") String phone,
                               @NotBlank(message = "Поле не должно быть пустым") @Email(message = "Email адрес должен быть в формате user@example.ru") String email,
                               @NotEmpty(message = "Поле не должно быть пустым") String theme,
                               @NotEmpty(message = "Поле не должно быть пустым") String textarea) {
}
