package ru.clapClass.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.domain.dto.briefcase.*;
import ru.clapClass.domain.dto.file.FileModelDto;
import ru.clapClass.domain.enums.TypeWarmUp;
import ru.clapClass.domain.mapper.BriefcaseMapper;
import ru.clapClass.domain.models.article.ArticleModel;
import ru.clapClass.domain.models.briefcase.BriefcaseModel;
import ru.clapClass.domain.models.briefcase.LevelBriefcaseModel;
import ru.clapClass.exception.BadRequest;
import ru.clapClass.exception.InternalServerError;
import ru.clapClass.repository.briefcase.BriefcaseRepository;
import ru.clapClass.repository.briefcase.LevelBriefcaseRepository;
import ru.clapClass.service.s3.ServiceS3;
import ru.clapClass.utils.FileCreate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@RequiredArgsConstructor
@Service
public class BriefcaseService {
    private final ServiceS3 serviceS3;
    private final EntityManager em;
    private final BriefcaseRepository briefcaseRepository;
    private final BriefcaseMapper briefcaseMapper;
    private final LevelBriefcaseRepository levelBriefcaseRepository;

    public ResponseEntity<?> addCase(BriefcaseRequest req, MultipartFile material) {
        try {
            var briefcase = briefcaseRepository.save(briefcaseMapper.toBriefcaseModel(req));
            var path = "case/" + briefcase.getId() + "/";
            serviceS3.putObject(path + "preview_file/" + req.getPreview_img().getOriginalFilename(), req.getPreview_img());
            briefcase.setPreview_img(FileCreate.addFileS3(req.getPreview_img(), path + "preview_file/" + req.getPreview_img().getOriginalFilename()));

            serviceS3.putObject(path + "rules/" + req.getRules().getOriginalFilename(), req.getRules());
            briefcase.setRules(FileCreate.addFileS3(req.getRules(), path + "rules/" + req.getRules().getOriginalFilename()));

            if (material != null) {
                serviceS3.putObject(path + "material/" + material.getOriginalFilename(), material);
                briefcase.setMaterial(FileCreate.addFileS3(material, path + "material/" + material.getOriginalFilename()));
            }
            briefcaseRepository.save(briefcase);
            return new ResponseEntity<>(briefcaseMapper.toResponseDto(briefcase), HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> editCase(BriefcaseRequest req, MultipartFile material) {
        try {

            var briefcase = briefcaseRepository.findById(req.getId());
            BriefcaseResponse res = null;

            if (briefcase.isPresent()) {
                briefcaseRepository.save(briefcaseMapper.partialUpdate(req, briefcase.get()));

                if (req.getPreview_img() != null) {
                    if (briefcase.get().getPreview_img() != null) {
                        serviceS3.deleteObject(briefcase.get().getPreview_img().getPath());
                    }
                    var path = "case/" + briefcase.get().getId() + "/";
                    serviceS3.putObject(path + "preview_file/" + req.getPreview_img().getOriginalFilename(), req.getPreview_img());
                    briefcase.get().setPreview_img(FileCreate.addFileS3(req.getPreview_img(), path + "preview_file/" + req.getPreview_img().getOriginalFilename()));
                }

                if (req.getRules() != null) {
                    if (briefcase.get().getRules() != null) {
                        serviceS3.deleteObject(briefcase.get().getRules().getPath());
                    }
                    var path = "case/" + briefcase.get().getId() + "/";
                    serviceS3.putObject(path + "rules/" + req.getRules().getOriginalFilename(), req.getRules());
                    briefcase.get().setRules(FileCreate.addFileS3(req.getRules(), path + "rules/" + req.getRules().getOriginalFilename()));
                }


                if (material != null) {
                    if (briefcase.get().getMaterial() != null) {
                        serviceS3.deleteObject(briefcase.get().getMaterial().getPath());
                    }
                    var path = "case/" + briefcase.get().getId() + "/";
                    serviceS3.putObject(path + "material/" + material.getOriginalFilename(), material);
                    briefcase.get().setMaterial(FileCreate.addFileS3(material, path + "material/" + material.getOriginalFilename()));
                }

                briefcaseRepository.save(briefcase.get());
                res = briefcaseMapper.toResponseDto(briefcase.get());
            }
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseEntity<?> list(String sort, String search, TypeWarmUp type, Long limit) {
        try {
            var list = new ArrayList<BriefcaseResponse>();
            var sortParam = sort != null && sort.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
            ExampleMatcher matcher = ExampleMatcher
                    .matchingAll()
                    .withIgnoreCase()
                    .withMatcher("title", contains().ignoreCase())
                    .withIgnoreNullValues()
                    .withIgnorePaths("annotation", "description", "author", "duration", "shows", "rating");

            var filters = BriefcaseModel
                    .builder()
                    .title(search)
                    .type(type)
                    .build();

            if (limit != null) {
                var pageRequest = PageRequest.of(0, Math.toIntExact(limit), sortParam, "createdAt");
                var briefcase = briefcaseRepository.findAll(Example.of(filters, matcher), pageRequest);
                for (var item : briefcase) {
                    list.add(briefcaseMapper.toResponseDto(item));
                }
                return new ResponseEntity<>(list, HttpStatus.OK);
            }

            var briefcase = briefcaseRepository.findAll(Example.of(filters, matcher), Sort.by(sortParam, "createdAt"));
            for (var item : briefcase) {
                list.add(briefcaseMapper.toResponseDto(item));
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> remove(Long id) {
        try {
            var briefcase = briefcaseRepository.findById(id);
            briefcaseRepository.deleteById(id);
            if (briefcase.isPresent() && briefcase.get().getPreview_img() != null) {
                var pathFile = briefcase.get().getPreview_img().getPath();
                serviceS3.deleteObject(pathFile);
            }

            if (briefcase.isPresent() && briefcase.get().getMaterial() != null) {
                var pathFile = briefcase.get().getMaterial().getPath();
                serviceS3.deleteObject(pathFile);
            }

            if (briefcase.isPresent() && briefcase.get().getRules() != null) {
                var pathFile = briefcase.get().getRules().getPath();
                serviceS3.deleteObject(pathFile);
            }

            if (briefcase.isPresent() && briefcase.get().getRules_video() != null) {
                var pathFile = briefcase.get().getRules_video().getPath();
                serviceS3.deleteObject(pathFile);
            }

            if (briefcase.isPresent() && !briefcase.get().getImages_slider().isEmpty()) {
                for (var item : briefcase.get().getImages_slider()) {
                    serviceS3.deleteObject(item.getPath());
                }
            }

            if (briefcase.isPresent() && !briefcase.get().getLevels().isEmpty()) {
                for (var item : briefcase.get().getLevels()) {
                    serviceS3.deleteObject(item.getFile().getPath());
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            throw new InternalServerError("error");
        }
    }

    @Transactional
    public ResponseEntity<?> addCaseImage(Long id, List<MultipartFile> images) {
        try {
            if (images.isEmpty()) {
                throw new BadRequest("Поле не может быть пустым", "file");
            }
            List<FileModelDto> res = new ArrayList<>();
            var briefcase = briefcaseRepository.findById(id);
            if (briefcase.isPresent()) {
                var list = briefcase.get().getImages_slider();
                var path = new StringBuilder().append("case/").append(id).append("/slider-images/");
                for (MultipartFile image : images) {
                    serviceS3.putObject(path + image.getOriginalFilename(), image);
                    list.add(FileCreate.addFileS3(image, path + image.getOriginalFilename()));
                }
                briefcase.get().setImages_slider(list);
                briefcaseRepository.save(briefcase.get());
                res = briefcaseMapper.toResponseSlider(briefcase.get().getImages_slider());
            }
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerError("error");
        }
    }

    public ResponseEntity<?> removeSliderImage(BriefcaseSliderRemoveRequest req) {
        var briefcase = briefcaseRepository.findById(req.caseId());
        if (briefcase.isPresent()) {
            var list = briefcase.get().getImages_slider();
            list.removeIf(n -> {
                if (n.getId() == req.imageId()) {
                    serviceS3.deleteObject(n.getPath());
                }
                return n.getId() == req.imageId();
            });
            briefcase.get().setImages_slider(list);
            briefcaseRepository.save(briefcase.get());
        }
        return new ResponseEntity<>(req.imageId(), HttpStatus.OK);
    }

    public ResponseEntity<?> addRulesVideo(BriefcaseRequest req) {
        try {
            if (req.getRules_video() == null) {
                throw new BadRequest("Поле не может быть пустым", "rules_video");
            }
            var briefcase = briefcaseRepository.findById(req.getId());
            BriefcaseResponse res = null;
            if (briefcase.isPresent()) {
                briefcase.get().setRules_video_description(req.getRules_video_description());
                var path = "case/" + req.getId() + "/rules_video/" + req.getRules_video().getOriginalFilename();
                briefcase.get().setRules_video(FileCreate.addFileS3(req.getRules_video(), path));
                briefcaseRepository.save(briefcase.get());
                res = briefcaseMapper.toResponseDto(briefcase.get());
                serviceS3.putObject(path, req.getRules_video());
            }
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerError("error");
        }
    }

    public ResponseEntity<?> editRulesVideo(BriefcaseRequest req, MultipartFile video) {
        try {
            var briefcase = briefcaseRepository.findById(req.getId());
            var path="";
            BriefcaseResponse res = null;
            if (briefcase.isPresent()) {
                briefcase.get().setRules_video_description(req.getRules_video_description());
                if (briefcase.get().getRules_video() != null && video != null) {
                    serviceS3.deleteObject(briefcase.get().getRules_video().getPath());
                    path = "case/" + req.getId() + "/rules_video/" + video.getOriginalFilename();
                }
                briefcase.get().setRules_video(FileCreate.addFileS3(video, path));
                res = briefcaseMapper.toResponseDto(briefcase.get());
                briefcaseRepository.save(briefcase.get());
            }
            serviceS3.putObject(path, req.getRules_video());
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerError("error");
        }
    }

    public ResponseEntity<?> addLevelCase(LevelBriefcaseRequest req) {
        try {
            var briefcase = briefcaseRepository.findById(req.getId());
            BriefcaseResponse res = null;
            if (briefcase.isPresent()) {
                var list = briefcase.get().getLevels();
                var level = LevelBriefcaseModel.builder().title(req.getTitle()).description(req.getDescription()).build();
                levelBriefcaseRepository.save(level);
                var path = "case/" + req.getId() + "/level/" + level.getId() + "/" + req.getFile().getOriginalFilename();
                level.setFile(FileCreate.addFileS3(req.getFile(), path));
                levelBriefcaseRepository.save(level);
                list.add(level);
                briefcaseRepository.save(briefcase.get());
                res = briefcaseMapper.toResponseDto(briefcase.get());
                serviceS3.putObject(path, req.getFile());
            }
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerError("error");
        }
    }

    public ResponseEntity<?> editLevelCase(LevelBriefcaseRequest req, MultipartFile file) {
        try {
            var level = levelBriefcaseRepository.findById(req.getLevelId());
            LevelBriefcaseModelDto res = null;
            var path = "";
            if (level.isPresent()) {
                level.get().setTitle(req.getTitle());
                level.get().setDescription(req.getDescription());
                levelBriefcaseRepository.save(level.get());
                if (level.get().getFile() != null && file != null) {
                    serviceS3.deleteObject(level.get().getFile().getPath());
                    path = "case/" + req.getId() + "/level/" + req.getLevelId() + '/' + file.getOriginalFilename();
                    level.get().setFile(FileCreate.addFileS3(file, path));
                    levelBriefcaseRepository.save(level.get());
                }
                res = briefcaseMapper.toLevelResponseDto(level.get());
                assert file != null ;
                serviceS3.putObject(path, file);
            }
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerError("error");
        }
    }

    public ResponseEntity<?> removeLevel(long level_id, long briefcase_id) {
        var briefcase = briefcaseRepository.findById(briefcase_id);
        if (briefcase.isPresent()) {
            var list = briefcase.get().getLevels();
            list.removeIf(n -> {
                if (n.getId() == level_id) {
                    serviceS3.deleteObject(n.getFile().getPath());
                }
                return n.getId() == level_id;
            });
            briefcase.get().setLevels(list);
            briefcaseRepository.save(briefcase.get());
        }
        return new ResponseEntity<>(level_id, HttpStatus.OK);
    }

    public ResponseEntity<?> getBriefcase(Long id) {
        try {
            var briefcase = briefcaseRepository.findById(id);
            if (briefcase.isPresent()) {
                return new ResponseEntity<>(briefcaseMapper.toResponseDto(briefcase.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new BadRequest("ошибка данных", "errors");
        }
    }

    public ResponseEntity<?> randomList(Long id, int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BriefcaseModel> q = cb.createQuery(BriefcaseModel.class);
        Root<BriefcaseModel> c = q.from(BriefcaseModel.class);
        Predicate p1 = cb.not(c.get("id").in(id));
        Order order = cb.asc(cb.function("RAND", null));
        q.select(c).where(p1).orderBy(order);
        var results = em.createQuery(q).setMaxResults(limit).getResultList().stream().map(briefcaseMapper::toResponseDto);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}