package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourseStringFormat;
import com.fdc.quartesure.quartesurebackend.POJO.PerformanceStringFormat;
import com.fdc.quartesure.quartesurebackend.rest.CurrentCourseRest;
import com.fdc.quartesure.quartesurebackend.service.CurrentCourseService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class CurrentCourseRestImpl implements CurrentCourseRest {

    @Autowired
    CurrentCourseService currentCourseService;


    @Override
    public Object getCourse(Map<String, String> requestMap) {
        try {
            Optional<CurrentCourseStringFormat> currentCourse = currentCourseService.getCourseFromLocalDB(requestMap);
            if(!currentCourse.isEmpty()) {
                System.out.println("8888888888888888888888888888888888");
                System.out.println(requestMap);
                System.out.println("8888888888888888888888888888888888");
                String refinedString = currentCourse.toString().substring(60);
                System.out.println("What is in refinedString"+refinedString.substring(3, refinedString.length()-2));
                if(requestMap.get("C").length()<2) {
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
                if(requestMap.get("C").length()<2) {
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
}
