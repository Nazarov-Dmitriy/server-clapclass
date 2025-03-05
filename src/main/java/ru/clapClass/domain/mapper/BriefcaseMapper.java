package ru.clapClass.domain.mapper;

import org.mapstruct.*;
import ru.clapClass.domain.dto.briefcase.BriefcaseRequest;
import ru.clapClass.domain.dto.briefcase.BriefcaseResponse;
import ru.clapClass.domain.dto.briefcase.LevelBriefcaseModelDto;
import ru.clapClass.domain.dto.file.FileModelDto;
import ru.clapClass.domain.models.briefcase.BriefcaseModel;
import ru.clapClass.domain.models.briefcase.LevelBriefcaseModel;
import ru.clapClass.domain.models.file.FileModel;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BriefcaseMapper {

    @Mapping(ignore = true, target = "rules")
    @Mapping(ignore = true, target = "preview_img")
    @Mapping(ignore = true, target = "material")
    BriefcaseModel toBriefcaseModel(BriefcaseRequest briefcaseModelDto);

    BriefcaseResponse toResponseDto(BriefcaseModel briefcaseModel);

    LevelBriefcaseModelDto toLevelResponseDto(LevelBriefcaseModel levelBriefcaseModel);

    List<FileModelDto> toResponseSlider(List<FileModel> briefcaseModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BriefcaseModel partialUpdate(BriefcaseRequest briefcaseModelDto, @MappingTarget BriefcaseModel briefcaseModel);
}