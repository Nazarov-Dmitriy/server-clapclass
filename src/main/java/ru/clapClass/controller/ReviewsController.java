package ru.clapClass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.domain.dto.reviews.ReviewsRequest;
import ru.clapClass.service.reviews.ReviewsService;


@RestController
@RequestMapping("reviews/")
@RequiredArgsConstructor
public class ReviewsController {
    private final ReviewsService reviewsService;

    @GetMapping(path = "/list")
    public ResponseEntity<?> getList() {
        return reviewsService.getList();
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addReviews(@Validated ReviewsRequest req, MultipartFile file) {
        return reviewsService.addReviews(req, file);
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<?> editReview(@Validated ReviewsRequest req, MultipartFile file) {
        return reviewsService.editReview(req, file);
    }

    @GetMapping(path = "/remove/{id}")
    public ResponseEntity<?> removeReviews(@PathVariable Long id) {
        return reviewsService.removeReviews(id);
    }
}
