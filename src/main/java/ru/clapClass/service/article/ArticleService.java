package ru.clapClass.service.article;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.domain.dto.article.ArticleRequest;
import ru.clapClass.domain.dto.article.ArticleResponse;
import ru.clapClass.domain.mapper.ArticleMapper;
import ru.clapClass.domain.models.article.ArticleModel;
import ru.clapClass.domain.models.article.TypeArticle;
import ru.clapClass.domain.models.article.favorite.ArticleFavorite;
import ru.clapClass.domain.models.article.favorite.ArticleFavoriteKey;
import ru.clapClass.exception.BadRequest;
import ru.clapClass.exception.InternalServerError;
import ru.clapClass.repository.ArticleRepository;
import ru.clapClass.repository.UserRepository;
import ru.clapClass.repository.favorite.FavoriteArticleRepository;
import ru.clapClass.service.mail.EmailService;
import ru.clapClass.service.s3.ServiceS3;
import ru.clapClass.utils.FileCreate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final UserRepository userRepository;
    private final EntityManager em;
    private final FavoriteArticleRepository articleFavoriteRepository;
    private final ServiceS3 serviceS3;

    @Autowired
    EmailService emailService;

    public ResponseEntity<?> addArticle(ArticleRequest req, MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new BadRequest("Поле не может быть пустым", "file");
            }
            var article = articleRepository.save(articleMapper.toArticleModel(req));

            var path = new StringBuilder().append("article/").append(article.getId()).append("/").append(file.getOriginalFilename());
            article.setFile(FileCreate.addFileS3(file, String.valueOf(path)));

            articleRepository.save(article);
            var subscriberUsers = userRepository.findBySubscribe(true);
            var pathMaterialSait = "/blog/" + article.getId();
            if (subscriberUsers.isPresent()) {
                emailService.sendMessageMaterial(subscriberUsers, pathMaterialSait, article);
            }
            serviceS3.putObject(String.valueOf(path), file);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequest("ошибка данных", "errors");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> getArticle(Long id) {
        try {
            var article = articleRepository.findById(id);
            if (article.isPresent()) {
                return new ResponseEntity<>(articleMapper.toArticleResponse(article.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new BadRequest("ошибка данных", "errors");
        }
    }

    public ResponseEntity<?> editArticle(ArticleRequest req, MultipartFile file) {
        try {
            var article = articleRepository.findById(req.id());
            StringBuilder path = new StringBuilder();
            if (article.isPresent()) {
                var edit_article = articleRepository.save(articleMapper.partialUpdate(req, article.get()));
                var currentFilePath = article.get().getFile().getPath();
                if (file != null) {
                    serviceS3.deleteObject(currentFilePath);
                    path = new StringBuilder().append("article/").append(article.get().getId()).append("/").append(file.getOriginalFilename());
                    edit_article.setFile(FileCreate.addFileS3(file, String.valueOf(path)));
                    articleRepository.save(edit_article);
                }
                assert file != null;
                serviceS3.putObject(String.valueOf(path), file);
                return new ResponseEntity<>(articleMapper.toArticleResponse(article.get()), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequest("ошибка данных", "errors");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> list(String sort, String search, TypeArticle type, Long limit) {
        try {
            var list = new ArrayList<ArticleResponse>();
            var sortParam = sort != null && sort.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
            ExampleMatcher matcher = ExampleMatcher
                    .matchingAll()
                    .withIgnoreCase()
                    .withMatcher("title", contains().ignoreCase())
                    .withIgnoreNullValues()
                    .withIgnorePaths("shows", "likes");

            var filters = ArticleModel
                    .builder()
                    .title(search)
                    .type(type)
                    .build();

            if (limit != null) {
                var pageRequest = PageRequest.of(0, Math.toIntExact(limit), sortParam, "createdAt");
                var article = articleRepository.findAll(Example.of(filters, matcher), pageRequest);
                for (var item : article) {
                    list.add(articleMapper.toArticleResponse(item));
                }
                return new ResponseEntity<>(list, HttpStatus.OK);
            }

            var article = articleRepository.findAll(Example.of(filters, matcher), Sort.by(sortParam, "createdAt"));
            for (var item : article) {
                list.add(articleMapper.toArticleResponse(item));
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> listFavorite(Long userId, String search) {
        try {
            var list = new ArrayList<ArticleResponse>();
            Optional<List<ArticleFavorite>> articleFavorites;
            if (!search.trim().isEmpty()) {
                articleFavorites = articleFavoriteRepository.findById_UserIdAndArticle_TitleContaining(userId, search);
            } else {
                articleFavorites = articleFavoriteRepository.findById_UserId(userId);
            }

            if (articleFavorites.isPresent()) {
                for (var item : articleFavorites.get()) {
                    list.add(articleMapper.toArticleResponse(item.getArticle()));
                }
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<?> remove(Long id) {
        try {
            var article = articleRepository.findById(id);
            articleRepository.deleteById(id);
            if (article.isPresent() && article.get().getFile() != null) {
                var pathFile = article.get().getFile().getPath();
                serviceS3.deleteObject(pathFile);
            }
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            throw new InternalServerError("error");
        }
    }

    @Transactional
    public ResponseEntity<?> setShow(Long id) {
        var article = articleRepository.findById(id);
        if (article.isPresent()) {
            article.get().setShows(article.get().getShows() + 1);
            articleRepository.save(article.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<?> setLike(Long id, Integer like, Integer dislike) {
        var article = articleRepository.findById(id).orElseThrow();
        if (like != null) {
            article.setLikes(article.getLikes() + like);
        } else if (dislike != null) {
            article.setLikes(Math.max(article.getLikes() - dislike, 0));
        }
        articleRepository.save(article);
        return new ResponseEntity<>(article.getLikes(), HttpStatus.OK);
    }

    public ResponseEntity<?> articleSiblingId(Long id) {
        HashMap<String, Long> list = new HashMap<>();
        var articlePrev = articleRepository.findTopByIdLessThanOrderByIdDesc(id).map(ArticleModel::getId);
        articlePrev.ifPresent(aLong -> list.put("prev", aLong));
        var articleNext = articleRepository.findFirstByIdIsGreaterThan(id).map(ArticleModel::getId);
        articleNext.ifPresent(aLong -> list.put("next", aLong));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<?> randomList(Long id, int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ArticleModel> q = cb.createQuery(ArticleModel.class);
        Root<ArticleModel> c = q.from(ArticleModel.class);
        Predicate p1 = cb.not(c.get("id").in(id));
        Order order = cb.asc(cb.function("RAND", null));
        q.select(c).where(p1).orderBy(order);
        var results = em.createQuery(q).setMaxResults(limit).getResultList().stream().map(articleMapper::toArticleResponse);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> addArticleFavorite(Long articleId, Long userId) {
        try {
            var user = userRepository.findById(userId);
            var article = articleRepository.findById(articleId);
            if (article.isPresent() && user.isPresent()) {
                var favoriteKey = new ArticleFavoriteKey(articleId, userId);
                var favorite = new ArticleFavorite(favoriteKey, user.get(), article.get());
                articleFavoriteRepository.save(favorite);
            }
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> removeArticleFavorite(Long articleId, Long userId) {
        try {
            var favoriteKey = new ArticleFavoriteKey(userId, articleId);
            articleFavoriteRepository.deleteById(favoriteKey);
            return new ResponseEntity<>(false, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> getArticleFavorite(Long articleId, Long userId) {
        try {
            var favoriteKey = new ArticleFavoriteKey(userId, articleId);
            var favorite = articleFavoriteRepository.findById(favoriteKey);
            if (favorite.isPresent()) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

