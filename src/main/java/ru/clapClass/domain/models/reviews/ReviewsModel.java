package ru.clapClass.domain.models.reviews;

import jakarta.persistence.*;
import lombok.*;
import ru.clapClass.domain.models.base.BaseEntity;
import ru.clapClass.domain.models.file.FileModel;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class ReviewsModel extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "author", nullable = false)
    private String author;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private FileModel file;
}
