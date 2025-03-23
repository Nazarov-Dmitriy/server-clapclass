package ru.clapClass.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.domain.dto.briefcase.BriefcaseRequest;
import ru.clapClass.domain.dto.briefcase.BriefcaseSliderRemoveRequest;
import ru.clapClass.domain.dto.briefcase.LevelBriefcaseRequest;
import ru.clapClass.domain.enums.TypeWarmUp;
import ru.clapClass.service.BriefcaseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("briefcase")
public class BriefcaseController {
    private final BriefcaseService briefcaseService;

    @Secured({"admin", "moderator"})
    @PostMapping(path = "/add")
    public ResponseEntity<?> addCase(@Validated(BriefcaseRequest.BriefcaseAdd.class) BriefcaseRequest req, MultipartFile material) {
        return briefcaseService.addCase(req, material);
    }

    @Secured({"admin", "moderator"})
    @PutMapping(path = "/edit")
    public ResponseEntity<?> editCase(@Validated(BriefcaseRequest.BriefcaseEdit.class) BriefcaseRequest req, MultipartFile material) {
        return briefcaseService.editCase(req, material);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(required = false) String sort, @RequestParam(required = false) String search, @RequestParam(required = false) TypeWarmUp type, @RequestParam(required = false) Long limit ,@RequestParam(required = false) Boolean enablePublished) {
        return briefcaseService.list(sort, search, type, limit, enablePublished);
    }

    @GetMapping("/list/favorite")
    public ResponseEntity<?> list(@RequestParam() Long user_id,
                                  @RequestParam(required = false) String search,
                                  @RequestParam(required = false) TypeWarmUp type
    ) {
        return briefcaseService.listFavorite(user_id, search, type);
    }

    @Secured({"admin", "moderator"})
    @GetMapping("/remove/{id}")
    public ResponseEntity<?> removeCase(@PathVariable Long id) {
        return briefcaseService.remove(id);
    }

    @Secured({"admin", "moderator"})
    @PostMapping(path = "/slider-image/add")
    public ResponseEntity<?> addCaseImage(Long id, List<MultipartFile> images) {
        return briefcaseService.addCaseImage(id, images);
    }

    @Secured({"admin", "moderator"})
    @PostMapping("/remove/slider-image")
    public ResponseEntity<?> removeSliderImage(@RequestBody BriefcaseSliderRemoveRequest req) {
        return briefcaseService.removeSliderImage(req);
    }

    @Secured({"admin", "moderator"})
    @PostMapping("/rules-video/add")
    public ResponseEntity<?> addRulesVideo(@Validated(BriefcaseRequest.BriefcaseVideoRules.class) BriefcaseRequest req) {
        return briefcaseService.addRulesVideo(req);
    }

    @Secured({"admin", "moderator"})
    @PostMapping("/rules-video/edit")
    public ResponseEntity<?> editRulesVideo(@Validated(BriefcaseRequest.BriefcaseVideoRulesEdit.class) BriefcaseRequest req, MultipartFile rules_video) {
        return briefcaseService.editRulesVideo(req, rules_video);
    }

    @Secured({"admin", "moderator"})
    @PostMapping("/level-case/add")
    public ResponseEntity<?> addLevelCase(@Validated(LevelBriefcaseRequest.LevelBriefcase.class) LevelBriefcaseRequest req) {
        return briefcaseService.addLevelCase(req);
    }

    @Secured({"admin", "moderator"})
    @PostMapping("/level-case/edit")
    public ResponseEntity<?> editLevelCase(@Validated(LevelBriefcaseRequest.LevelBriefcaseEdit.class) LevelBriefcaseRequest req, MultipartFile file) {
        return briefcaseService.editLevelCase(req, file);
    }

    @Secured({"admin", "moderator"})
    @GetMapping("/remove-level")
    public ResponseEntity<?> removeLevel(@RequestParam() long level_id, @RequestParam() long briefcase_id) {
        return briefcaseService.removeLevel(level_id, briefcase_id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBriefcase(@PathVariable Long id) {
        return briefcaseService.getBriefcase(id);
    }

    @GetMapping("/random-list")
    public ResponseEntity<?> randomList(@RequestParam() Long id, @RequestParam(required = false, defaultValue = "3") int limit) {
        return briefcaseService.randomList(id, limit);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<?> setShow(@PathVariable Long id) {
        return briefcaseService.setShow(id);
    }

    @GetMapping("/favorite/add")
    public ResponseEntity<?> addFavorite(@RequestParam Long briefcase_id, @RequestParam Long user_id) {
        return briefcaseService.addFavorite(briefcase_id, user_id);
    }

    @GetMapping("/favorite/remove")
    public ResponseEntity<?> removeFavorite(@RequestParam Long briefcase_id, @RequestParam Long user_id) {
        return briefcaseService.removeFavorite(briefcase_id, user_id);
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getFavorite(@RequestParam Long briefcase_id, @RequestParam Long user_id) {
        return briefcaseService.getFavorite(briefcase_id, user_id);
    }

    @GetMapping("/rating/add")
    public ResponseEntity<?> addRating(@RequestParam Long briefcase_id, @RequestParam Long user_id, @RequestParam int rating) {
        return briefcaseService.addRating(briefcase_id, user_id, rating);
    }

    @GetMapping("/rating")
    public ResponseEntity<?> getRating(@RequestParam Long briefcase_id, @RequestParam Long user_id) {
        return briefcaseService.getRating(briefcase_id, user_id);
    }

    @Secured({"admin", "moderator"})
    @PutMapping("/published/{id}")
    public ResponseEntity<?> setPublished(@PathVariable Long id) {
        return briefcaseService.setPublished(id);
    }
}
