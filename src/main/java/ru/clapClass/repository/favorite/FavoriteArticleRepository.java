package ru.clapClass.repository.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clapClass.domain.models.article.favorite.ArticleFavorite;
import ru.clapClass.domain.models.article.favorite.ArticleFavoriteKey;

@Repository
public interface FavoriteArticleRepository extends JpaRepository<ArticleFavorite, ArticleFavoriteKey> {
}