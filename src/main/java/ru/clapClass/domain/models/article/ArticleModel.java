package ru.clapClass.domain.models.article;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import ru.clapClass.domain.models.article.favorite.ArticleFavorite;
import ru.clapClass.domain.models.base.BaseEntity;
import ru.clapClass.domain.models.file.FileModel;
import ru.clapClass.domain.models.user.User;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "article")
public class ArticleModel extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "published", nullable = false)
    @ColumnDefault("false")
    private boolean published;

    @Lob
    @Column(name = "article", nullable = false, columnDefinition = "LONGTEXT")
    private String article;

    @Column(name = "shows")
    private int shows;

    @Column(name = "likes")
    private int likes;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeArticle type;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private FileModel file;

    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<ArticleFavorite> favorite;
}

