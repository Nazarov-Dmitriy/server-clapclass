package ru.clapClass.domain.models.article.favorite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleFavoriteKey implements Serializable {
    @Column(name = "user_id")
    Long userId;

    @Column(name = "article_id")
    Long articleId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ArticleFavoriteKey that = (ArticleFavoriteKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(articleId, that.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, articleId);
    }
}
