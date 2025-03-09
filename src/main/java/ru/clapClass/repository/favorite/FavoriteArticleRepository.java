package ru.clapClass.repository.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clapClass.domain.models.article.favorite.ArticleFavorite;
import ru.clapClass.domain.models.article.favorite.ArticleFavoriteKey;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteArticleRepository extends JpaRepository<ArticleFavorite, ArticleFavoriteKey> {
    Optional<List<ArticleFavorite>> findById_UserIdAndArticle_TitleContaining(Long userId, String search);

    Optional<List<ArticleFavorite>> findById_UserId(Long userId);
}