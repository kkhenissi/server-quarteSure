package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourseStringFormat;
import com.fdc.quartesure.quartesurebackend.POJO.CurrentProgrammeStringFormat;
import com.fdc.quartesure.quartesurebackend.rest.CurrentProgrammeRest;
import com.fdc.quartesure.quartesurebackend.service.CurrentCourseService;
import com.fdc.quartesure.quartesurebackend.service.CurrentProgrammeService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class CurrentProgrammeRestImpl implements CurrentProgrammeRest {

    @Autowired
    CurrentProgrammeService currentProgrammeService;
    @Override
    public Object getTodayProgramme(Map<String, String> requestMap) {
        try {
            Optional<CurrentProgrammeStringFormat> currentProgramme = currentProgrammeService.getTodayProgrammeFromLocalDB(requestMap);
            if(!currentProgramme.isEmpty()) {
                System.out.println("8888888888888888888888888888888888");
                System.out.println(requestMap);
                System.out.println("8888888888888888888888888888888888");
                String refinedString = currentProgramme.toString().substring(60);
                System.out.println("What is in refinedString"+refinedString.substring(3, refinedString.length()-2));
               // if(requestMap.get("C").length()<3) {
                    return refinedString.substring(3, refinedString.length()-2);
             //   }else {
             //       return refinedString.substring(3, refinedString.length()-2);
             //   }

            }
            System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
            System.out.println(requestMap);

            return  currentProgrammeService.getTodayProgramme(requestMap);

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Object getYesterdayProgramme(Map<String, String> requestMap) {
        try {
            Optional<CurrentProgrammeStringFormat> currentProgramme = currentProgrammeService.getYesterdayProgrammeFromLocalDB(requestMap);
                    // getTodayProgrammeFromLocalDB(requestMap);
            if(!currentProgramme.isEmpty()) {
                System.out.println("8888888888888888888888888888888888");
                System.out.println(requestMap);
                System.out.println("8888888888888888888888888888888888");
                String refinedString = currentProgramme.toString().substring(60);
                System.out.println("What is in refinedString"+refinedString.substring(3, refinedString.length()-2));
                // if(requestMap.get("C").length()<3) {
                return refinedString.substring(3, refinedString.length()-2);
                //   }else {
                //       return refinedString.substring(3, refinedString.length()-2);
                //   }

            }
            System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
            System.out.println(requestMap);

            return  currentProgrammeService.getYesterdayProgramme(requestMap);

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
