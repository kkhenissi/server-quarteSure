package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourse;
import com.fdc.quartesure.quartesurebackend.POJO.Participant;
import com.fdc.quartesure.quartesurebackend.service.CurrentCourseService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class CurrentCourseServiceImpl implements CurrentCourseService {
    @Override
    public ResponseEntity<String> getCourse(Map<String, String> requestMap) {


//        Date aujourdhui = new Date();
//        String currentUrl="";
//        currentUrl = "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/"+aujourdhui+"/"+refCourse.getReunion()+"/"+refCourse.getCourse()+"/performances-detaillees/pretty";
//
//        System.out.println("*****************************************************");
//        System.out.println(refCourse.getClass());
//        System.out.println(aujourdhui);
//        System.out.println(currentUrl);
//        System.out.println("*****************************************************");
        try {
            log.info("Inside signup {}",requestMap);
            if(validateRefCourse(requestMap)) {
                String aujourdhui = new SimpleDateFormat("ddMMyyyy").format(new Date());
                String currentUrl="";
                currentUrl = "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/"+aujourdhui+"/"+requestMap.get("R")+"/"+requestMap.get("C")+"/performances-detaillees/pretty";
                URL url = new URL(currentUrl);
                log.info("CurrentUrl {}",currentUrl);
                // "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/09022024/R7/C5/performances-detaillees/pretty"
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                // Check if Connect is made
                int responseCode = conn.getResponseCode();

                // 200 Ok
                if(responseCode != 200) {
                    throw new RuntimeException("HttpResponseCode:"+responseCode);
                } else {
                    StringBuilder informationRetrived = new StringBuilder();
                    Scanner scanner = new Scanner(url.openStream());
                    while (scanner.hasNext()){
                        informationRetrived.append(scanner.nextLine());
                    }
                    // close the scanner
                    scanner.close();

                    JsonObject dataObject = new JsonParser().parse((informationRetrived.toString())).getAsJsonObject();

                    JsonElement bruteParticipants = dataObject.get("participants");
                    log.info("bruteParticipants {}",bruteParticipants);
                    Type listType = new TypeToken<List<Participant>>() {}.getType();
                    Type allureType = new TypeToken<String>() {}.getType();
                    List<Participant> jsonParticipants = new Gson().fromJson(bruteParticipants, listType);
                    String jsonAllure = new Gson().fromJson(dataObject.get("allure"),allureType);

                    System.out.println(jsonParticipants);
                    CurrentCourse translateCurrentCourse = new CurrentCourse();

                    translateCurrentCourse.setParticipants((ArrayList<Participant>) jsonParticipants);
                    translateCurrentCourse.setAllure(jsonAllure);
                    Gson gson = new Gson();


                    log.info("What is in translateCurrentCourse {}",gson.toJson(translateCurrentCourse));

                    return new ResponseEntity<>(gson.toJson(translateCurrentCourse.toString()) ,HttpStatus.OK);
                }
            }


        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
                // QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private boolean validateRefCourse(Map<String,String> requestMap) {
        return requestMap.containsKey("R") && requestMap.containsKey("C") && requestMap.containsKey("specialite");

    }
}
