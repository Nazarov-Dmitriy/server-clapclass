package ru.clapClass.servise.article;

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
import ru.clapClass.domain.models.article.FavoriteArticleModel;
import ru.clapClass.domain.models.article.FavoriteKey;
import ru.clapClass.domain.models.article.TypeArticle;
import ru.clapClass.exception.BadRequest;
import ru.clapClass.exception.InternalServerError;
import ru.clapClass.repository.ArticleRepository;
import ru.clapClass.repository.UserRepository;
import ru.clapClass.repository.favorite.FavoriteArticleRepository;
import ru.clapClass.servise.mail.EmailService;
import ru.clapClass.servise.s3.ServiceS3;
import ru.clapClass.utils.FileCreate;
import ru.clapClass.utils.FileDelete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

    @Transactional
    public ResponseEntity<Void> addArticle(ArticleRequest req, MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new BadRequest("Поле не может быть пустым", "file");
            }
            var article = articleRepository.save(articleMapper.toArticleModel(req));
            var path = new StringBuilder().append("article/").append(article.getId()).append("/").append(file.getOriginalFilename());
            var result = serviceS3.putObject(String.valueOf(path), file);
            article.setFile(FileCreate.addFileS3(file, path));
            articleRepository.save(article);
            var subscriberUsers = userRepository.findBySubscribe(true);
            var pathMaterial = "/blog/" + article.getId();

            if (subscriberUsers.isPresent()) {
                emailService.sendMessageMaterial(subscriberUsers, pathMaterial, article);
            }
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

            if (article.isPresent()) {
                var edit_article = articleRepository.save(articleMapper.partialUpdate(req, article.get()));
                var currentFile = article.get().getFile();
                if (file != null) {
                    var pathDeleteFile = (currentFile.getPath().split("/"));
                    var directoryPath = pathDeleteFile[0] + "/" + pathDeleteFile[1] + "/" + pathDeleteFile[2];
                    FileDelete.deleteFile(directoryPath);

                    var path = new StringBuilder();
                    path.append("files/article/").append(edit_article.getId()).append("/");
                    var new_file = FileCreate.addFile(file, path);
                    edit_article.setFile(new_file);
                    articleRepository.save(edit_article);
                }
                return new ResponseEntity<>(articleMapper.toArticleResponse(article.get()), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequest("ошибка данных", "errors");
        }
    }

    public ResponseEntity<?> list(String sort, String search, TypeArticle type, Long limit) {
        try {
            var list = new ArrayList<ArticleResponse>();
            var sortParam = sort != null && sort.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
            ExampleMatcher matcher = ExampleMatcher
                    .matchingAll()
                    .withIgnoreCase("likes")
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

    @Transactional
    public ResponseEntity<?> remove(Long id) {
        try {
            var article = articleRepository.findById(id);
            articleRepository.deleteById(id);

            if (article.isPresent() && article.get().getFile() != null) {
                var pathFile = (article.get().getFile().getPath().split("/"));
                var directoryPath = pathFile[0] + "/" + pathFile[1] + "/" + pathFile[2];
                FileDelete.deleteFile(directoryPath, true);
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
            var favoriteKey = new FavoriteKey(articleId, userId);
            var favorite = new FavoriteArticleModel(favoriteKey);
            articleFavoriteRepository.save(favorite);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> removeArticleFavorite(Long articleId, Long userId) {
        try {
            var favoriteKey = new FavoriteKey(articleId, userId);
            articleFavoriteRepository.deleteByPkFavorite(favoriteKey);
            return new ResponseEntity<>(false, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> getArticleFavorite(Long articleId, Long userId) {
        try {
            var favoriteKey = new FavoriteKey(articleId, userId);
            var favorite = articleFavoriteRepository.findByPkFavorite(favoriteKey);
            if (favorite != null) {
                return new ResponseEntity<>(favorite.getFavorite(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

