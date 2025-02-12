package ru.clapClass.domain.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.clapClass.domain.models.user.User;

public record ArticleRequest(Long id, @NotBlank(message = "Поле не должно быть пустым") String title,
                             @NotBlank(message = "Поле не должно быть пустым") String article,
                             @NotEmpty(message = "Поле не должно быть пустым") String type, @NotNull User author) {
}
