package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.TokenTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenDao extends JpaRepository<TokenTable, Integer> {

    Optional<TokenTable> findByToken(@Param("token") String token);
}
