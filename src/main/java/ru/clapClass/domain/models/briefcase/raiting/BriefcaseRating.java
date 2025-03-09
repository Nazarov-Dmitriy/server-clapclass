package ru.clapClass.domain.models.briefcase.raiting;

import jakarta.persistence.*;
import lombok.*;
import ru.clapClass.domain.models.briefcase.BriefcaseModel;
import ru.clapClass.domain.models.user.User;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "briefcase_rating")
public class BriefcaseRating {
    @EmbeddedId
    BriefcaseRatingKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("briefcaseId")
    @JoinColumn(name = "briefcase_id")
    BriefcaseModel briefcase;

    int rating;
}
