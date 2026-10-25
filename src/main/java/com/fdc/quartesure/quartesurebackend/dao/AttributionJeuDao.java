package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.AttributionJeu;
import com.fdc.quartesure.quartesurebackend.POJO.Fillol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributionJeuDao extends JpaRepository<AttributionJeu, Long> {

    /** Les 50 dernières parts d'un filleul, les plus récentes d'abord */
    List<AttributionJeu> findTop50ByFillolOrderByDateEnvoiDescIdDesc(Fillol fillol);
}
