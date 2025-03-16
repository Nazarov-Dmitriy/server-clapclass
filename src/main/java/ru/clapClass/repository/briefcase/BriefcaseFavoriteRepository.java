package ru.clapClass.repository.briefcase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clapClass.domain.models.briefcase.favorite.BriefcaseFavorite;
import ru.clapClass.domain.models.briefcase.favorite.BriefcaseFavoriteKey;

@Repository
public interface BriefcaseFavoriteRepository extends JpaRepository<BriefcaseFavorite, BriefcaseFavoriteKey> {
}
