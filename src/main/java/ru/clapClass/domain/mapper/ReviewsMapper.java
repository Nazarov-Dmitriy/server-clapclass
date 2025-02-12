package ru.clapClass.domain.mapper;

import org.mapstruct.*;
import ru.clapClass.domain.dto.reviews.ReviewsRequest;
import ru.clapClass.domain.dto.reviews.ReviewsResponse;
import ru.clapClass.domain.models.reviews.ReviewsModel;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewsMapper {

    ReviewsModel toReviewsModel(ReviewsRequest reviews);

    @Mapping(source = "file.path", target = "filePath")
    ReviewsResponse toReviewsResponse(ReviewsModel reviewsModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReviewsModel partialUpdate(ReviewsRequest reviews, @MappingTarget ReviewsModel reviewsModel);
}