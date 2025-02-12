package ru.clapClass.repository.reviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clapClass.domain.models.reviews.ReviewsModel;

@Repository
public interface ReviewsRepository extends JpaRepository<ReviewsModel, Long> {
}