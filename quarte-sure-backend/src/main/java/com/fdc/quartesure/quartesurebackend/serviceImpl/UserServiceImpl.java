package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.JWT.CustomerUsersDetailsService;
import com.fdc.quartesure.quartesurebackend.JWT.JwtFilter;
import com.fdc.quartesure.quartesurebackend.JWT.JwtUtil;
import com.fdc.quartesure.quartesurebackend.POJO.EmailTemplateName;
import com.fdc.quartesure.quartesurebackend.POJO.Token;
import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.constants.QuarteSureConstants;
import com.fdc.quartesure.quartesurebackend.dao.TokenDao;
import com.fdc.quartesure.quartesurebackend.dao.UserDao;
import com.fdc.quartesure.quartesurebackend.service.EmailService;
import com.fdc.quartesure.quartesurebackend.service.UserService;
import com.fdc.quartesure.quartesurebackend.utils.EmailUtils;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
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
    @Autowired
    private PasswordEncoder passwordEncode;
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private EmailService emailService;
    @Value("${spring.application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        try {
            log.info("Inside signup {}",requestMap);
            if(validateSignUpMap(requestMap)) {
                User user = userDao.findByEmail(requestMap.get("email"));
                if(Objects.isNull(user)){
                    user = getUserFromMap(requestMap);
                    log.info("What is in user hire ==> {}",user);
                    userDao.save(user);

                    sendValidationEmail(user);
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

    private void sendValidationEmail(User user) throws MessagingException {

        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(), EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        // generate a token
        String generetedToken = generateActivationCode(5);
        UUID myuuid = UUID.randomUUID();
        long highbits = myuuid.getMostSignificantBits();
        long lowbits = myuuid.getLeastSignificantBits();
        var token = Token.builder()
                .id((int) (highbits+lowbits))
                .token(generetedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        log.info("What is in  token inside == {}",token.toString());
        tokenDao.save(token);
        return generetedToken;

    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());  // 0..9
            codeBuilder.append(characters.charAt(randomIndex));

        }
        return codeBuilder.toString();
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("inside login {}",requestMap);
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password")));
            log.info("inside isAuthenticated ",auth.isAuthenticated());
            if(auth.isAuthenticated()) {

                if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    User user = userDao.findByEmail(requestMap.get("email"));
                    if(user.getLastConnexion().equals(Instant.now().toString().substring(0,10))) {
                        // do noThing
                    } else {
                        if(user.getCredit() > 0) {
                            user.setCredit(user.getCredit() - 1);
                        }
                        user.setLastConnexion(Instant.now().toString().substring(0,10));
                        log.info("What is in user when connected ==> {}",user);
                        userDao.save(user);
                   }



                    return new ResponseEntity<String>("{\"token\":\""+jwtUtil.generateTocken(
                            customerUsersDetailsService.getUserDetail().getEmail(),
                            customerUsersDetailsService.getUserDetail().getRole(),
                            customerUsersDetailsService.getUserDetail().getCredit().toString(),
                            customerUsersDetailsService.getUserDetail().getSelectedOptions().toString())+"\"}", HttpStatus.OK);

                } else {
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
         //   if (jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
//            }else {
//                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
//            }
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

    @Override
    public ResponseEntity<String> updateSelectedOptions(Map<String, String> requestMap) {
        log.info("requestMap.get(\"selectedOptions\") {}==>",requestMap.get("selectedOptions"));
        return null;
    }


    @Override
    @Transactional
    public ResponseEntity<String> activateAccount(String token) throws MessagingException {

        Token savedToken = tokenDao.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(()-> new RuntimeException("Invalid token"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw  new RuntimeException("Activation token has expired, a new token has been sent to the same email address");
        }
        var user = userDao.findById(savedToken.getUser().getUser_id())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        user.setStatus("true");
        userDao.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenDao.save(savedToken);
        return null;
    }


    // arrivé jusquau 1:18:18

    private boolean validateSignUpMap(Map<String,String> requestMap) {
        return requestMap.containsKey("firstName") && requestMap.containsKey("lastName") && requestMap.containsKey("phoneNumber") && requestMap.containsKey("email") && requestMap.containsKey("password");

    }

    private  User getUserFromMap(Map<String, String> requestMap) {

        User user = new User();
        user.setFirstName(requestMap.get("firstName"));
        user.setLastName(requestMap.get("lastName"));
        user.setPhoneNumber(requestMap.get("phoneNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password")); // passwordEncode.encode();
        user.setStatus("false");
        user.setCredit(5);
        user.setSelectedOptions(requestMap.get("selectedOptions"));
        user.setLastConnexion(Instant.now().toString().substring(5,10));
        user.setSubscribeDate(Instant.now().toString().substring(0,10));
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

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
           User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
           log.info("Inside changePassword {}",userObj);
           if(!userObj.equals(null)) {
               if(userObj.getPassword().equals(requestMap.get("oldPassword"))) {
                   userObj.setPassword(requestMap.get("newPassword"));
                   userDao.save(userObj);
                   return QuarteSureUtils.getResponseEntity("Password Updated Successfully.",HttpStatus.OK);

               }
               return QuarteSureUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_GATEWAY);

           }
           return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
