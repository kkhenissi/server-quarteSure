package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MouvementDao extends JpaRepository<Mouvement,Integer> {

 //   List<MouvementWrapper> getAll();
}
