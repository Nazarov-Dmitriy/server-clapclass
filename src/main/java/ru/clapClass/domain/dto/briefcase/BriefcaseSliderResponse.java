package ru.clapClass.domain.dto.briefcase;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import ru.clapClass.domain.dto.file.FileModelDto;
import ru.clapClass.domain.enums.TypeWarmUp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class BriefcaseSliderResponse implements Serializable {
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
    int rating;
    FileModelDto preview_img;
    FileModelDto rules;
    FileModelDto material;

}