package ru.clapClass.domain.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record FormRequest(@NotBlank(message = "Поле не должно быть пустым") String name,
                          @NotBlank(message = "Поле не должно быть пустым") String phone,
                          @NotBlank(message = "Поле не должно быть пустым") @Email(message = "Email адрес должен быть в формате user@example.ru") String email,
                          @NotEmpty(message = "Поле не должно быть пустым") String question) {
}
