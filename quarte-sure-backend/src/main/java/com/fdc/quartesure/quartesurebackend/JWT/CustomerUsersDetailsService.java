package com.fdc.quartesure.quartesurebackend.JWT;


import com.fdc.quartesure.quartesurebackend.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {
    @Autowired
    UserDao userDao;

    private com.fdc.quartesure.quartesurebackend.POJO.User userDetail;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername{} ",username);
        userDetail = userDao.findByEmailId(username);
        log.info("userDetail==>{} ",userDetail);
        if(!Objects.isNull(userDetail))
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        else
            throw new UsernameNotFoundException("User not found.");
    }
    public com.fdc.quartesure.quartesurebackend.POJO.User getUserDetail() {

      //  com.fdc.quartesure.quartesurebackend.POJO.User user = userDetail;
        return userDetail;
    }
}
