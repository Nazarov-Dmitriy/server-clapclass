package ru.clapClass.service.social;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.clapClass.domain.dto.social.SocialRequest;
import ru.clapClass.domain.models.social.SocialModel;
import ru.clapClass.exception.BadRequest;
import ru.clapClass.exception.InternalServerError;
import ru.clapClass.repository.social.SocialRepository;

@Service
@RequiredArgsConstructor
public class SocialService {
    private final SocialRepository socialRepository;

    public ResponseEntity<?> addSocial(SocialRequest socialRequest) {
        try {
            var social = SocialModel.builder().name(socialRequest.name()).link(socialRequest.link()).build();
            socialRepository.save(social);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new BadRequest("ошибка данных", "errors");
        }
    }

    public ResponseEntity<?> getList() {
        return new ResponseEntity<>(socialRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> editSocial(SocialRequest socialRequest) {
        try {
            var social = socialRepository.findByName(socialRequest.name());
            if (social.isPresent()) {
                social.get().setLink(socialRequest.link());
                socialRepository.save(social.get());
            }
            return new ResponseEntity<>(social, HttpStatus.OK);
        } catch (Exception e) {
            throw new BadRequest("ошибка данных", "errors");
        }
    }

    @Transactional
    public ResponseEntity<?> remove(String name) {
        try {
            socialRepository.deleteByName(name);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerError("error");
        }
    }
}




