package ru.clapClass.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.clapClass.domain.models.user.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserResponse {
    private Long id;
    private String email;
    private Role role;
    private String name;
    private String position;
    private String place_work;
    private String phone;
    private Boolean completed_profile;
    private String city;
    private String avatar;
    private Boolean subscribe;
}
