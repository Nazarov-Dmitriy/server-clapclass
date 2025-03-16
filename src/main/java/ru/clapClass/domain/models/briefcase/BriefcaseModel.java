package ru.clapClass.domain.models.briefcase;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import ru.clapClass.domain.enums.TypeWarmUp;
import ru.clapClass.domain.models.base.BaseEntity;
import ru.clapClass.domain.models.briefcase.favorite.BriefcaseFavorite;
import ru.clapClass.domain.models.briefcase.raiting.BriefcaseRating;
import ru.clapClass.domain.models.file.FileModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BRIEFCASE")
public class BriefcaseModel extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "annotation", columnDefinition = "MEDIUMTEXT")
    private String annotation;

    @Column(name = "published", nullable = false)
    @ColumnDefault("false")
    private boolean published;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "author")
    private String author;

    @Column(name = "duration")
    private String duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeWarmUp type;

    @Column(name = "shows")
    private int shows;

    @Column(name = "rating")
    private double rating;

    @Lob
    @Column(name = "rules_video_description", columnDefinition = "MEDIUMTEXT")
    String rules_video_description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private FileModel preview_img;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private FileModel rules;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private FileModel material;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<FileModel> images_slider = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private FileModel  rules_video;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LevelBriefcaseModel> levels = new ArrayList<>();

    @OneToMany(mappedBy = "briefcase" , cascade = CascadeType.ALL, orphanRemoval = true)
    Set<BriefcaseFavorite> favorites;

    @OneToMany(mappedBy = "briefcase" , cascade = CascadeType.ALL, orphanRemoval = true)
    Set<BriefcaseRating> briefcase_ratings;
}

