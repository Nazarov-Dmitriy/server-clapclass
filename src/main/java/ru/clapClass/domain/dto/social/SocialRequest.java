package ru.clapClass.domain.dto.social;

import jakarta.validation.constraints.NotBlank;
import ru.clapClass.domain.models.social.SocialModel;

/**
 * DTO for {@link SocialModel}
 */

public record SocialRequest(@NotBlank(message = "Поле не должно быть пустым") String name,
                            @NotBlank(message = "Поле не должно быть пустым") String link) {
}