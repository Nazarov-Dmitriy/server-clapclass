package ru.clapClass.repository.briefcase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clapClass.domain.models.briefcase.raiting.BriefcaseRating;
import ru.clapClass.domain.models.briefcase.raiting.BriefcaseRatingKey;

import java.util.Optional;

@Repository
public interface BriefcaseRatingRepository extends JpaRepository<BriefcaseRating, BriefcaseRatingKey> {
    @Query("select avg(rating) from BriefcaseRating  where id.briefcaseId = ?1")
    Optional<Double> findAvgRating(Long id_briefcaseId);
}
