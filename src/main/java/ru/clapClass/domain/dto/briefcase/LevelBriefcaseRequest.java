package ru.clapClass.domain.dto.briefcase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelBriefcaseRequest {
    Long id;
    Long levelId;
    @NotBlank(message = "Поле не должно быть пустым", groups = {LevelBriefcase.class, LevelBriefcaseEdit.class})
    String title;
    @NotBlank(message = "Поле не должно быть пустым", groups = {LevelBriefcase.class,LevelBriefcaseEdit.class})
    String description;
    @NotNull(message = "Поле не должно быть пустым", groups = {LevelBriefcase.class})
    MultipartFile file;

    /**
     * Группа проверок для основной информации
     */
    public interface LevelBriefcase {
    }

    /**
     * Группа проверок для основной информации
     */
    public interface LevelBriefcaseEdit {
    }
}
