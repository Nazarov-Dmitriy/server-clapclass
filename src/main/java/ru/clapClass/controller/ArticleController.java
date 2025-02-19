package ru.clapClass.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.domain.dto.article.ArticleRequest;
import ru.clapClass.domain.models.article.TypeArticle;
import ru.clapClass.servise.article.ArticleService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("article")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping(path = "/add")
    public ResponseEntity<?> addArticle(@Validated ArticleRequest req, MultipartFile file) {
        return articleService.addArticle(req, file);
    }

    @PostMapping(path = "/file")
    public ResponseEntity<?> addArticle(MultipartFile file, Long id) throws IOException {
        return articleService.file(file, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticle(@PathVariable Long id) {
        return articleService.getArticle(id);
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<?> editArticle(@Validated ArticleRequest req, MultipartFile file) {
        return articleService.editArticle(req, file);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(required = false) String sort, @RequestParam(required = false) String search, @RequestParam(required = false) TypeArticle type, @RequestParam(required = false) Long limit) {
        return articleService.list(sort, search, type, limit);
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<?> removeArticle(@PathVariable Long id) {
        return articleService.remove(id);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<?> setShow(@PathVariable Long id) {
        return articleService.setShow(id);
    }

    @GetMapping("/like")
    public ResponseEntity<?> setLike(@RequestParam Long id, @RequestParam(required = false) Integer like, @RequestParam(required = false) Integer dislike) {
        return articleService.setLike(id, like, dislike);
    }

    @GetMapping("/sibling-id/{id}")
    public ResponseEntity<?> articleSiblingId(@PathVariable Long id) {
        return articleService.articleSiblingId(id);
    }

    @GetMapping("/random-list")
    public ResponseEntity<?> randomList(@RequestParam() Long id, @RequestParam(required = false, defaultValue = "3") int limit) {
        return articleService.randomList(id, limit);
    }

    @GetMapping("/add-favorite")
    public ResponseEntity<?> addArticleFavorite(@RequestParam Long article_id, @RequestParam Long user_id) {
        return articleService.addArticleFavorite(article_id, user_id);
    }

    @GetMapping("/remove-favorite")
    public ResponseEntity<?> removeArticleFavorite(@RequestParam Long article_id, @RequestParam Long user_id) {
        return articleService.removeArticleFavorite(article_id, user_id);
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getArticleFavorite(@RequestParam Long article_id, @RequestParam Long user_id) {
        return articleService.getArticleFavorite(article_id, user_id);
    }
}
