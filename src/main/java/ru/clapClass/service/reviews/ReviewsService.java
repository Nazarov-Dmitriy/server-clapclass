package ru.clapClass.service.reviews;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.domain.dto.reviews.ReviewsRequest;
import ru.clapClass.domain.dto.reviews.ReviewsResponse;
import ru.clapClass.domain.mapper.ReviewsMapper;
import ru.clapClass.exception.BadRequest;
import ru.clapClass.exception.InternalServerError;
import ru.clapClass.repository.reviews.ReviewsRepository;
import ru.clapClass.service.s3.ServiceS3;
import ru.clapClass.utils.FileCreate;

import java.io.IOException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;
    private final ReviewsMapper reviewsMapper;
    private final ServiceS3 serviceS3;

    public ResponseEntity<?> addReviews(ReviewsRequest req, MultipartFile file) {
        try {
            if (file != null) {
                var reviews = reviewsRepository.save(reviewsMapper.toReviewsModel(req));

                var path = new StringBuilder().append("reviews/").append(reviews.getId()).append("/").append(file.getOriginalFilename());
                serviceS3.putObject(String.valueOf(path), file);
                var new_file = FileCreate.addFileS3(file, String.valueOf(path));
                reviews.setFile(new_file);
                reviewsRepository.save(reviews);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new BadRequest("Поле не может быть пустым", "file");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> editReview(ReviewsRequest req, MultipartFile file) {
        try {
            var reviews = reviewsRepository.findById(req.id());
            if (reviews.isPresent()) {
                var edit_reviews = reviewsRepository.save(reviewsMapper.partialUpdate(req, reviews.get()));
                var currentFilePath = edit_reviews.getFile().getPath();

                if (file != null) {
                    serviceS3.deleteObject(currentFilePath);
                    var path = new StringBuilder().append("reviews/").append(edit_reviews.getId()).append("/").append(file.getOriginalFilename());
                    serviceS3.putObject(String.valueOf(path), file);
                    edit_reviews.setFile(FileCreate.addFileS3(file, String.valueOf(path)));
                    reviewsRepository.save(edit_reviews);
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequest("ошибка данных", "errors");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> getList() {
        try {
            var reviews = reviewsRepository.findAll();
            var list = new ArrayList<ReviewsResponse>();
            if (!reviews.isEmpty()) {
                for (var item : reviews) {
                    list.add(reviewsMapper.toReviewsResponse(item));
                }
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerError("Ошибка сервера");
        }
    }

    @Transactional
    public ResponseEntity<?> removeReviews(Long id) {
        try {
            var reviews = reviewsRepository.findById(id);
            reviewsRepository.deleteById(id);

            if (reviews.isPresent() && reviews.get().getFile() != null) {
                serviceS3.deleteObject(reviews.get().getFile().getPath());
            }
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}



