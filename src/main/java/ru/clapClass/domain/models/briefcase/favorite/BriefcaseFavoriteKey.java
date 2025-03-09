package ru.clapClass.domain.models.briefcase.favorite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BriefcaseFavoriteKey implements Serializable {
    @Column(name = "user_id")
    Long userId;

    @Column(name = "briefcase_id")
    Long briefcaseId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BriefcaseFavoriteKey that = (BriefcaseFavoriteKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(briefcaseId, that.briefcaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, briefcaseId);
    }
}
