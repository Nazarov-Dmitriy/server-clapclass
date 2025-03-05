package ru.clapClass.domain.dto.briefcase;

import lombok.Value;
import ru.clapClass.domain.dto.file.FileModelDto;
import ru.clapClass.domain.models.briefcase.LevelBriefcaseModel;
import java.io.Serializable;

/**
 * DTO for {@link LevelBriefcaseModel}
 */

@Value
public class LevelBriefcaseModelDto implements Serializable {
    long id;
    String title;
    String description;
    FileModelDto file;
}
