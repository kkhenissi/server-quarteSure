package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.JeuParrain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JeuParrainDao extends JpaRepository<JeuParrain, Long> {

    /** Les 100 derniers jeux d'un parrain, les plus récents d'abord */
    List<JeuParrain> findTop100ByUserIdOrderByDepartMsDescIdDesc(Integer userId);

    Optional<JeuParrain> findByUserIdAndCourseKey(Integer userId, String courseKey);

    Optional<JeuParrain> findByIdAndUserId(Long id, Integer userId);
}
