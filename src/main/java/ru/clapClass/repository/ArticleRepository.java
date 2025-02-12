package ru.clapClass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.clapClass.domain.models.article.ArticleModel;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleModel, Long>, PagingAndSortingRepository<ArticleModel, Long> {
    Optional<ArticleModel> findFirstByIdIsGreaterThan(Long id);

    Optional<ArticleModel> findTopByIdLessThanOrderByIdDesc(Long id);
}
