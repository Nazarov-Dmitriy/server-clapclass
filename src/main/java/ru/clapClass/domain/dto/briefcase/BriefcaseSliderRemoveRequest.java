package ru.clapClass.domain.dto.briefcase;

import jakarta.validation.constraints.NotBlank;

public record BriefcaseSliderRemoveRequest(
        @NotBlank(message = "Поле не должно быть пустым") Long caseId,
        @NotBlank(message = "Поле не должно быть пустым") Long imageId
) {
}
