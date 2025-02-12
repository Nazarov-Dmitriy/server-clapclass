package ru.clapClass.domain.dto.reviews;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.clapClass.domain.models.reviews.ReviewsModel;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link ReviewsModel}
 */
public record ReviewsRequest(@NotNull(message = "Поле не должно быть пустым") LocalDate date,
                         @NotBlank(message = "Поле не должно быть пустым") String description,
                         @NotBlank(message = "Поле не должно быть пустым") String author,
                         Long id) implements Serializable {
}