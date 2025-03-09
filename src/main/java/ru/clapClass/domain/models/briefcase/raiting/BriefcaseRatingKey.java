package ru.clapClass.domain.models.briefcase.raiting;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BriefcaseRatingKey implements Serializable {
    @Column(name = "user_id")
    Long userId;

    @Column(name = "briefcase_id")
    Long briefcaseId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BriefcaseRatingKey that = (BriefcaseRatingKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(briefcaseId, that.briefcaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, briefcaseId);
    }
}
