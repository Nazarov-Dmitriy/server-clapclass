package ru.clapClass.domain.dto.briefcase.rank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankRequest {
    Long userId;
    String briefcaseId;
}
