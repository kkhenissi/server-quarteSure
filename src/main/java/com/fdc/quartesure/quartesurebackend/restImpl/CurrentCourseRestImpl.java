package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourseStringFormat;
import com.fdc.quartesure.quartesurebackend.POJO.PerformanceStringFormat;
import com.fdc.quartesure.quartesurebackend.dto.DetailPerfRequest;
import com.fdc.quartesure.quartesurebackend.dto.LightPerfRequest;
import com.fdc.quartesure.quartesurebackend.dto.RefCourse;
import com.fdc.quartesure.quartesurebackend.rest.CurrentCourseRest;
import com.fdc.quartesure.quartesurebackend.service.CurrentCourseService;
import com.fdc.quartesure.quartesurebackend.serviceImpl.UserServiceImpl;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class CurrentCourseRestImpl implements CurrentCourseRest {

    @Autowired
    CurrentCourseService currentCourseService;
    @Autowired
    UserServiceImpl userService;


    @Override
    public Object getCourse(Map<String, String> requestMap) {

        try {
            Optional<CurrentCourseStringFormat> currentCourse = currentCourseService.getCourseFromLocalDB(requestMap);
            boolean isAdmin = userService.isAdmin();
            log.info(" quelle rolz is ==, {}", isAdmin);
            //  ADMIN → ignorer la DB et recharger depuis PMU
            if (isAdmin) {
                log.info(" ADMIN connecté → rechargement forcé depuis PMU");
                return currentCourseService.getCourse(requestMap);
            }
            if(!currentCourse.isEmpty()) {
                System.out.println("8888888888888888888888888888888888");
                System.out.println(requestMap);
                System.out.println("8888888888888888888888888888888888");
                String refinedString = currentCourse.toString().substring(60);
                System.out.println("What is in refinedString"+refinedString.substring(3, refinedString.length()-2));
                if(requestMap.get("C").length()<3) {
                    return refinedString.substring(2, refinedString.length()-2);
                }else {
                    return refinedString.substring(3, refinedString.length()-2);
                }

            }

           return  currentCourseService.getCourse(requestMap);

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Object getPerformances(Map<String, String> requestMap) {
        try {
            Optional<PerformanceStringFormat> currentPerformance = currentCourseService.getPerformanceFromLocalDB(requestMap);
            System.out.println("SSSSSSSSSSSS  SSSSSSSSSSSSSSSSSSSSSS");
            System.out.println(currentPerformance);

            if(!currentPerformance.isEmpty()) {
                String refinedString = currentPerformance.toString().substring(58);
                if(requestMap.get("C").length()<3) {
                    return refinedString.substring(2, refinedString.length()-2);
                }else {
                    return refinedString.substring(3, refinedString.length()-2);
                }
            }
            return  currentCourseService.getPerfomances(requestMap);

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Object getOrdreArrivee(int numOrdre, Map<String, String> requestMap) {
        log.info(" What is in numOrde {}",numOrdre);
        log.info("📦 Contenu brut de requestMap : {}", requestMap);

        ResponseEntity ordreArrivee = currentCourseService.getOrdreArriveeByNumOrdre(requestMap, numOrdre);

//        if (ordreArrivee == null || ordreArrivee.isMissingNode()) {
//            return ResponseEntity.notFound().build();
//        }

        return ResponseEntity.ok(ordreArrivee);
    }

    @Override
    public Object getRapportsDefinitifs(int numOrdre, Map<String, String> requestMap) {
        return currentCourseService.getRapportsDefinitifs(requestMap, numOrdre);
    }

    @Override
    public Object getLightPerformances(LightPerfRequest request) {

        String nomCheval = request.getNomCheval();

        // refCourse n’est pas un String → il faut le récupérer comme un sous-objet
        Object refCourseObj = request.getRefCourse();

        // convertir refCourse → RefCourse
        RefCourse refCourse = new ObjectMapper().convertValue(refCourseObj, RefCourse.class);

        return currentCourseService.getLightPerf(request);
    }

    @Override
    public Object getDetailPerformances(DetailPerfRequest request) {
        return currentCourseService.getDetailPerf(request);
    }

}
