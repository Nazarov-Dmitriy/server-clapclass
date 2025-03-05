package ru.clapClass.domain.dto.briefcase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.domain.dto.auth.UserRequest;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BriefcaseRequest {
    Long id;
    @NotBlank(message = "Поле не должно быть пустым", groups = {BriefcaseAdd.class , BriefcaseEdit.class})
    String title;
    @NotBlank(message = "Поле не должно быть пустым" ,groups = {BriefcaseAdd.class , BriefcaseEdit.class})
    String annotation;
    @NotBlank(message = "Поле не должно быть пустым" ,groups = {BriefcaseAdd.class , BriefcaseEdit.class})
    String description;
    @NotBlank(message = "Поле не должно быть пустым" ,groups = {BriefcaseAdd.class , BriefcaseEdit.class})
    String author;
    @NotBlank(message = "Поле не должно быть пустым" ,groups = {BriefcaseAdd.class , BriefcaseEdit.class})
    String duration;
    @NotBlank(message = "Поле не должно быть пустым" ,groups = {BriefcaseAdd.class , BriefcaseEdit.class})
    String type;

    @NotNull(message = "Поле не должно быть пустым" , groups = {BriefcaseAdd.class})
    MultipartFile preview_img;

    @NotNull(message = "Поле не должно быть пустым" , groups = {BriefcaseAdd.class})
    MultipartFile rules;

    @NotBlank(message = "Поле не должно быть пустым" ,groups = {BriefcaseVideoRules.class, BriefcaseVideoRulesEdit.class})
    String rules_video_description;

    @NotNull(message = "Поле не должно быть пустым" , groups = {BriefcaseVideoRules.class})
    MultipartFile rules_video;

    /**
     * Группа проверок для основной информации
     */
    public interface BriefcaseAdd {
    }

    /**
     * Группа проверок для основной информации редактирвоание
     */
    public interface BriefcaseEdit {
    }

    /**
     * Группа проверок для основной добавить видое правила
     */
    public interface BriefcaseVideoRules {
    }

    /**
     * Группа проверок для основной редактировать видое правила
     */
    public interface BriefcaseVideoRulesEdit {
    }


}
