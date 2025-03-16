package ru.clapClass.domain.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.clapClass.domain.models.article.TypeArticle;
import ru.clapClass.domain.models.user.Role;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ArticleResponse implements Serializable {
    private Long id;
    private String title;
    private String article;
    private int shows;
    private int likes;
    private TypeArticle type;
    private boolean published;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDateTime updatedAt;
    private String file;
    private UserDto author;

    /**
     * DTO for {@link ru.clapClass.domain.models.user.User}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class UserDto implements Serializable {
        private Long id;
        private String email;
        private Role role;
        private String name;
        private String phone;
        private String place_work;
        private String position;
        private String city;
    }
}