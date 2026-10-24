package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.Fillol;
import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FillolDao extends JpaRepository<Fillol,Integer> {

    @Override
    Optional<Fillol> findById(Integer integer);

    Fillol findByEmail(@Param("email") String email);

    /** Tous les filleuls (invités) d'un parrain */
    List<Fillol> findByUser(User user);

  //  List<Fillol> getAllFillol();
    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

}
