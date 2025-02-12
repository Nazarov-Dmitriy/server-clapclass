package ru.clapClass.servise.reviews;

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
import ru.clapClass.utils.FileCreate;
import ru.clapClass.utils.FileDelete;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;
    private final ReviewsMapper reviewsMapper;

    public ResponseEntity<?> addReviews(ReviewsRequest req, MultipartFile file) {
        try {
            if (file != null) {
//                var reviews = new ReviewsModel();
//                reviews.setDate(req.date());
//                reviews.setDescription(req.description());
//                reviews.setAuthor(req.author());
                var reviews = reviewsRepository.save(reviewsMapper.toReviewsModel(req));

                var path = new StringBuilder();
                path.append("files/reviews/").append(reviews.getId()).append("/");
                var new_file = FileCreate.addFile(file, path);
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
                var currentFile = edit_reviews.getFile();

                if (file != null) {
                    if (currentFile != null) {
                        var pathFile = (currentFile.getPath().split("/"));
                        var directoryPath = pathFile[0] + "/" + pathFile[1] + "/" + pathFile[2];
                        FileDelete.deleteFile(directoryPath);
                    }
                    var path = new StringBuilder();
                    path.append("files/reviews/").append(edit_reviews.getId()).append("/");
                    var new_file = FileCreate.addFile(file, path);
                    edit_reviews.setFile(new_file);
                    reviewsRepository.save(edit_reviews);
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequest("ошибка данных", "errors");
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
                var pathFile = (reviews.get().getFile().getPath().split("/"));
                var directoryPath = pathFile[0] + "/" + pathFile[1] + "/" + pathFile[2];
                FileDelete.deleteFile(directoryPath, true);
            }
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}



