package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.JWT.CustomerUsersDetailsService;
import com.fdc.quartesure.quartesurebackend.JWT.JwtFilter;
import com.fdc.quartesure.quartesurebackend.JWT.JwtUtil;
import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.constants.QuarteSureConstants;
import com.fdc.quartesure.quartesurebackend.dao.UserDao;
import com.fdc.quartesure.quartesurebackend.service.UserService;
import com.fdc.quartesure.quartesurebackend.utils.EmailUtils;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    EmailUtils emailUtils;
    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        try {
            log.info("Inside signup {}",requestMap);
            if(validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userDao.save(getUserFromMap(requestMap));
                    return QuarteSureUtils.getResponseEntity("Succesfully Registred.",HttpStatus.OK);

                } else {
                    return QuarteSureUtils.getResponseEntity("Email already exist.",HttpStatus.BAD_REQUEST);
                }

               // userDao.save(requestMap);
            } else {
              return  QuarteSureUtils.getResponseEntity(QuarteSureConstants.BAD_REQUEST,HttpStatus.BAD_REQUEST);
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password")));
            log.info("inside isAuthenticated ",auth.isAuthenticated());
            if(auth.isAuthenticated()) {

                if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\""+jwtUtil.generateTocken(
                            customerUsersDetailsService.getUserDetail().getEmail(),
                            customerUsersDetailsService.getUserDetail().getRole())+"\"}", HttpStatus.OK);

                }else {
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval."+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex) {
            log.error("{}",ex);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex) {
           ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {

        try {
            if(jwtFilter.isAdmin()) {
              Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
                log.info("requestMap.get(\"status\") {}==>",requestMap.get("status"));
              if(!optional.isEmpty()) {
                  userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                  sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(),userDao.getAllAdmin());
                  return QuarteSureUtils.getResponseEntity("User status update successfully",HttpStatus.OK);
              } else {
                 return QuarteSureUtils.getResponseEntity("User not found",HttpStatus.OK);
              }

            }else {
                return QuarteSureUtils.getResponseEntity(QuarteSureConstants.UNAUTHORIZED,HttpStatus.UNAUTHORIZED);
            }


        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        //

    }



    // arrivé jusquau 1:18:18

    private boolean validateSignUpMap(Map<String,String> requestMap) {
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password");

    }

    private  User getUserFromMap(Map<String, String> requestMap) {

        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account approuved","USER:-"+user+"\n is approuved by \nADMIN:-"+jwtFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account disabled","USER:-"+user+"\n is disabled by \nADMIN:-"+jwtFilter.getCurrentUser(), allAdmin);
        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
       return QuarteSureUtils.getResponseEntity("true",HttpStatus.OK);
    }


}
