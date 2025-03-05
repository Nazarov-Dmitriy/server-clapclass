package ru.clapClass.repository.briefcase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clapClass.domain.models.briefcase.LevelBriefcaseModel;

@Repository
public interface LevelBriefcaseRepository extends JpaRepository<LevelBriefcaseModel, Long> {
}
