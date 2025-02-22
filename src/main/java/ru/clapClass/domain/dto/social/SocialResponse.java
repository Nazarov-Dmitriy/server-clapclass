package ru.clapClass.domain.dto.social;

import ru.clapClass.domain.models.social.SocialModel;

import java.io.Serializable;

/**
 * DTO for {@link SocialModel}
 */
public record SocialResponse(String name,
                             String link) implements Serializable {
}