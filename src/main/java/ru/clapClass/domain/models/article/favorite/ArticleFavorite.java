package ru.clapClass.domain.models.article.favorite;

import jakarta.persistence.*;
import lombok.*;
import ru.clapClass.domain.models.article.ArticleModel;

import ru.clapClass.domain.models.user.User;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "article_favorite")
public class ArticleFavorite {
    @EmbeddedId
    ArticleFavoriteKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("articleId")
    @JoinColumn(name = "article_id")
    ArticleModel article;
}
