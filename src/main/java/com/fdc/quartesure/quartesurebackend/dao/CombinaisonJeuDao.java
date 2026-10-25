package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.CombinaisonJeu;
import com.fdc.quartesure.quartesurebackend.POJO.Fillol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface CombinaisonJeuDao extends JpaRepository<CombinaisonJeu, Long> {

    /** Parmi les identifiants demandés, uniquement les combinaisons qui appartiennent à ce filleul. */
    @Query("select c from CombinaisonJeu c where c.id in :ids and c.attribution.fillol = :fillol")
    List<CombinaisonJeu> findOwned(@Param("ids") Collection<Long> ids, @Param("fillol") Fillol fillol);
}
