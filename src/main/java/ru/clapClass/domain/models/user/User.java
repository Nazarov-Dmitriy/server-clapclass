package ru.clapClass.domain.models.user;


import jakarta.persistence.*;
import lombok.*;
import ru.clapClass.domain.models.article.ArticleModel;
import ru.clapClass.domain.models.article.FavoriteArticleModel;
import ru.clapClass.domain.models.file.FileModel;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USERS")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "place_work")
    private String place_work;

    @Column(name = "position")
    private String position;

    @Column(name = "completed_profile")
    private Boolean completed_profile;

    @Column(name = "city")
    private String city;

    @Column(name = "subscribe")
    private Boolean subscribe = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private FileModel avatar;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private ArticleModel article;

    @PrePersist
    public void prePersist() {
        if (completed_profile == null) {
            completed_profile = false;
        }
        if (subscribe == null) {
            subscribe = false;
        }
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    Set<FavoriteArticleModel> favorite;
}
