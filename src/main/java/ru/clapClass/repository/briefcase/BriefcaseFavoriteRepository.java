package ru.clapClass.repository.briefcase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clapClass.domain.enums.TypeWarmUp;
import ru.clapClass.domain.models.briefcase.favorite.BriefcaseFavorite;
import ru.clapClass.domain.models.briefcase.favorite.BriefcaseFavoriteKey;

import java.util.List;
import java.util.Optional;

@Repository
public interface BriefcaseFavoriteRepository extends JpaRepository<BriefcaseFavorite, BriefcaseFavoriteKey> {
    Optional<List<BriefcaseFavorite>> findById_UserIdAndBriefcase_TitleContaining(Long id_userId, String briefcase_title);

    Optional<List<BriefcaseFavorite>> findById_UserId(Long userId);

    Optional<List<BriefcaseFavorite>> findById_UserIdAndBriefcase_TypeLike(Long idUserId, TypeWarmUp briefcaseType);

    Optional<List<BriefcaseFavorite>> findById_UserIdAndBriefcase_TypeLikeAndBriefcase_TitleContaining(Long id_userId, TypeWarmUp briefcase_type, String briefcase_title );
}
