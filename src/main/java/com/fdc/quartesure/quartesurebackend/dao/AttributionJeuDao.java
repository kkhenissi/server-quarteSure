package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.AttributionJeu;
import com.fdc.quartesure.quartesurebackend.POJO.Fillol;
import com.fdc.quartesure.quartesurebackend.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AttributionJeuDao extends JpaRepository<AttributionJeu, Long> {

    /** Les 50 dernières parts d'un filleul, les plus récentes d'abord */
    List<AttributionJeu> findTop50ByFillolOrderByDateEnvoiDescIdDesc(Fillol fillol);

    /** Parts envoyées par ce parrain pour la course qui part à cette heure (epoch ms), les plus récentes d'abord */
    @Query("select a from AttributionJeu a where a.fillol.user = :parrain and a.departMs = :departMs "
            + "order by a.dateEnvoi desc, a.id desc")
    List<AttributionJeu> findByParrainAndDepart(@Param("parrain") User parrain, @Param("departMs") Long departMs);

    /** Même chose quand l'heure de départ n'a pas été transmise : par libellé de course, depuis une date */
    @Query("select a from AttributionJeu a where a.fillol.user = :parrain and a.course = :course "
            + "and a.dateEnvoi >= :since order by a.dateEnvoi desc, a.id desc")
    List<AttributionJeu> findByParrainAndCourseSince(@Param("parrain") User parrain, @Param("course") String course,
                                                    @Param("since") LocalDateTime since);
}
