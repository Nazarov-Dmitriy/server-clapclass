package ru.clapClass.repository.briefcase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.clapClass.domain.models.briefcase.BriefcaseModel;

@Repository
public interface BriefcaseRepository extends JpaRepository<BriefcaseModel, Long>, PagingAndSortingRepository<BriefcaseModel, Long> {
}
