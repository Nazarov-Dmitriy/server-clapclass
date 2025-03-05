package ru.clapClass.domain.models.file;

import jakarta.persistence.*;
import lombok.*;
import ru.clapClass.domain.models.article.ArticleModel;
import ru.clapClass.domain.models.briefcase.BriefcaseModel;
import ru.clapClass.domain.models.reviews.ReviewsModel;
import ru.clapClass.domain.models.user.User;

@ToString
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FILES")
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "BLOB")
    private String path;

    @Column(nullable = false)
    private long size;

    @OneToOne(mappedBy = "avatar")
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "file", cascade = CascadeType.PERSIST)
    private ArticleModel article;

    @OneToOne(mappedBy = "file")
    private ReviewsModel reviews;
}

