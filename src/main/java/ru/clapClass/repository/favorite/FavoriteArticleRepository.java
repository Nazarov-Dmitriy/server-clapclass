package ru.clapClass.repository.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clapClass.domain.models.article.FavoriteArticleModel;
import ru.clapClass.domain.models.article.FavoriteKey;

@Repository
public interface FavoriteArticleRepository extends JpaRepository<FavoriteArticleModel, FavoriteKey> {
    void deleteByPkFavorite(FavoriteKey key);
    FavoriteArticleModel findByPkFavorite(FavoriteKey key);
//
//    List<FavoriteModel> findByPkFavorite_UserId(long userId);
//
//    List<FavoriteModel> findByPkFavorite_UserIdAndNews_TagsIn(Long pkFavorite_userId, List<TagsModel> news_tags);
}