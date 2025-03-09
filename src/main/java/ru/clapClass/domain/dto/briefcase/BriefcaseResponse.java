package ru.clapClass.domain.dto.briefcase;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import ru.clapClass.domain.dto.file.FileModelDto;
import ru.clapClass.domain.enums.TypeWarmUp;
import ru.clapClass.domain.models.briefcase.BriefcaseModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link BriefcaseModel}
 */
@Value
public class BriefcaseResponse implements Serializable {
    @JsonFormat(pattern = "dd.MM.yyyy")
    LocalDateTime createdAt;
    @JsonFormat(pattern = "dd.MM.yyyy")
    LocalDateTime updatedAt;
    Long id;
    String title;
    String annotation;
    String description;
    String author;
    String duration;
    TypeWarmUp type;
    int shows;
    double rating;
    FileModelDto preview_img;
    FileModelDto rules;
    FileModelDto material;
    String rules_video_description;
    FileModelDto rules_video;
    List<FileModelDto> images_slider;
    List<LevelBriefcaseModelDto> levels;
}