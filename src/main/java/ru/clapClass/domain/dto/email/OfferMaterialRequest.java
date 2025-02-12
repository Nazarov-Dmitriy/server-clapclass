package ru.clapClass.domain.dto.email;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class OfferMaterialRequest {
    String name;
    String email;
    @NotBlank(message = "Поле не должно быть пустым")
    String title;
    @NotBlank(message = "Поле не должно быть пустым")
    String type;

}
