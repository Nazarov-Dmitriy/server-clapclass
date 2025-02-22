package ru.clapClass.repository.social;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clapClass.domain.models.social.SocialModel;

import java.util.Optional;

@Repository
public interface SocialRepository extends JpaRepository<SocialModel, Long> {
    Optional<SocialModel> findByName(String name);

    void deleteByName(String name);
}