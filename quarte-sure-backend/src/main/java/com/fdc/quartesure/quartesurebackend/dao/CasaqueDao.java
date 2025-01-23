package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.Casaque;
import com.fdc.quartesure.quartesurebackend.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CasaqueDao extends JpaRepository<Casaque,String> {

    Casaque findByCasaqueId(@Param("casaqueId") String casaqueId);
}
