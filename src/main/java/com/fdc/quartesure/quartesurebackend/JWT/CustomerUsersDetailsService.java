package com.fdc.quartesure.quartesurebackend.JWT;


import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private User userDetail;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername{} ",username.toString());
        userDetail = userDao.findByEmail(username);
        if (userDetail == null) {
            // Toujours lancer une exception si l'utilisateur n'existe pas
            throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
        }
        log.info("userDetail==>{} ",userDetail.toString());
        if(!Objects.isNull(userDetail))

            return new org.springframework.security.core.userdetails.User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        else
            throw new UsernameNotFoundException("User not found.");
    }
    public User getUserDetail() {

        User user = userDetail;
        return user;
    }
}
