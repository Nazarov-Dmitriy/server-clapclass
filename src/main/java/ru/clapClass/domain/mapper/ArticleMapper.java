package ru.clapClass.domain.mapper;

import org.mapstruct.*;
import ru.clapClass.domain.dto.article.ArticleRequest;
import ru.clapClass.domain.dto.article.ArticleResponse;
import ru.clapClass.domain.models.article.ArticleModel;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {
    ArticleModel toArticleModel(ArticleRequest article);

    @InheritInverseConfiguration(name = "toArticleModel")
    @Mapping(source = "file.path", target = "file")
    ArticleResponse toArticleResponse(ArticleModel newsModel);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ArticleModel partialUpdate(ArticleRequest articleRequest, @MappingTarget ArticleModel articleModel);
}
