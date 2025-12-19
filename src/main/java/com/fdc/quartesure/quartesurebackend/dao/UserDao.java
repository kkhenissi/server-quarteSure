package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User,Integer> {

    @Override
    Optional<User> findById(Integer integer);

    User findByEmail(@Param("email") String email);

    List<UserWrapper> getAllUser();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    @Transactional
    @Modifying
    String updateSelectedOptions(@Param("selectedOptions") String selectedOptions, @Param("id") Integer id);

    List<String> getAllAdmin();

    // ===================== FORGOT PASSWORD =====================
    Optional<User> findByResetPasswordToken(String resetPasswordToken);

}
