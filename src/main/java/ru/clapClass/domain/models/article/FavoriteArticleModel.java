package ru.clapClass.domain.models.article;

import jakarta.persistence.*;
import lombok.*;
import ru.clapClass.domain.models.user.User;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "favorite_article")
public class FavoriteArticleModel {
    @EmbeddedId
    private FavoriteKey pkFavorite = new FavoriteKey();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("articleId")
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    ArticleModel article;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    Boolean favorite;

    public FavoriteArticleModel(FavoriteKey pk) {
        var newsPk = new ArticleModel();
        newsPk.setId(pk.getArticleId());
        var userPk = new User();
        userPk.setId(pk.getUserId());
        this.pkFavorite = pk;
        this.article = newsPk;
        this.user = userPk;
        favorite = true;
    }
}


