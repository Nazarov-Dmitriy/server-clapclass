package ru.clapClass.domain.models.briefcase;

import jakarta.persistence.*;
import lombok.*;
import ru.clapClass.domain.models.file.FileModel;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder()
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "level_briefcase")
public class LevelBriefcaseModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private FileModel file;
}

