package ru.clapClass.domain.dto.reviews;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.clapClass.domain.models.reviews.ReviewsModel;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link ReviewsModel}
 */
public record ReviewsResponse(Long id, @JsonFormat(pattern = "dd.MM.yyyy") LocalDate date, String description,
                              String author,
                              String filePath) implements Serializable {
}