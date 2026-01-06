package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.constants.QuarteSureConstants;
import com.fdc.quartesure.quartesurebackend.rest.ChangePasswordRequest;
import com.fdc.quartesure.quartesurebackend.rest.UserRest;
import com.fdc.quartesure.quartesurebackend.service.UserService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return userService.signup(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(
                QuarteSureConstants.SOMETHING_WENT_WRONG,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(
                QuarteSureConstants.SOMETHING_WENT_WRONG,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            return userService.getAllUser();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(
                new ArrayList<>(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            userService.update(requestMap);
            return QuarteSureUtils.getResponseEntity(
                    "User status update successfully",
                    HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(
                QuarteSureConstants.SOMETHING_WENT_WRONG,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<String> confirm(@RequestParam String token) {
        try {
            userService.activateAccount(token);
            return QuarteSureUtils.getResponseEntity(
                    "User status update successfully",
                    HttpStatus.OK
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(
                QuarteSureConstants.SOMETHING_WENT_WRONG,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return userService.checkToken();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(
                QuarteSureConstants.SOMETHING_WENT_WRONG,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody ChangePasswordRequest request
    ) {
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Utilisateur non authentifié");
        }

        userService.changePassword(request);

        return ResponseEntity.ok(
                Map.of("message", "Mot de passe changé avec succès")
        );
    }

    @Override
    public ResponseEntity<String> updateSelectedOptions(String id, String request) {
        try {
            return userService.updateSelectedOptions(id, request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(
                QuarteSureConstants.SOMETHING_WENT_WRONG,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // ===================== FORGOT PASSWORD =====================
    @Override
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            return userService.forgotPassword(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            return QuarteSureUtils.getResponseEntity(
                    QuarteSureConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ===================== RESET PASSWORD =====================
    @Override
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String password
    ) {
        try {
            return userService.resetPassword(token, password);
        } catch (Exception ex) {
            ex.printStackTrace();
            return QuarteSureUtils.getResponseEntity(
                    QuarteSureConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
