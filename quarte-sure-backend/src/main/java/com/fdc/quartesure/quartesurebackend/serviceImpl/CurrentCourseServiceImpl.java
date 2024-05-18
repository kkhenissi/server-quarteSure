package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.Course;
import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourse;
import com.fdc.quartesure.quartesurebackend.POJO.InfosParticipant;
import com.fdc.quartesure.quartesurebackend.POJO.Participant;
import com.fdc.quartesure.quartesurebackend.dao.CourseDao;
import com.fdc.quartesure.quartesurebackend.dao.UserDao;
import com.fdc.quartesure.quartesurebackend.service.CurrentCourseService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class CurrentCourseServiceImpl implements CurrentCourseService {

    @Autowired
    private CourseDao courseDao;
    @Override
    public ResponseEntity<String> getCourse(Map<String, String> requestMap) {

        try {
            log.info("Inside signup {}",requestMap);
            if(validateRefCourse(requestMap)) {
                // look data from local database
                String toDay = new SimpleDateFormat("ddMMyyyy").format(new Date());
                if(1==2) { // if selected course is allway in local databasecouse
                    // TOODO extract data from local database and return it

                } else {
                    String currentUrl="";
                    currentUrl = "https://online.turfinfo.api.pmu.fr/rest/client/1/programme/"+toDay+"/"+requestMap.get("R")+"/"+requestMap.get("C")+"/participants?specialisation=INTERNET";
                    URL url = new URL(currentUrl);
                    log.info("CurrentUrl {}",currentUrl);
                    // https://online.turfinfo.api.pmu.fr/rest/client/1/programme/09052024/R1/C7/participants?specialisation=INTERNET
                    //   this.urlParticipants="/"+dateCourse.toISOString().split('-')[2].substring(0,2)+dateCourse.toISOString().split('-')[1]+dateCourse.toISOString().split('-')[0]+"/"+this.refCourse.R+"/"+this.refCourse.C+"/participants?specialisation=INTERNET"
                    // "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/09022024/R7/C5/performances-detaillees/pretty"

                    // "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/"+toDay+"/"+requestMap.get("R")+"/"+requestMap.get("C")+"/performances-detaillees/pretty";
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

                        JsonElement bruteParticipants = dataObject;
                        log.info("bruteParticipants {}",bruteParticipants);
                        Type listType = new TypeToken<List<Participant>>() {}.getType();
                        Type allureType = new TypeToken<String>() {}.getType();
                        // List<InfosParticipant> jsonParticipants = (List<InfosParticipant>) bruteParticipants;
                        String jsonAllure = new Gson().fromJson(dataObject.get("allure"),allureType);

//                    System.out.println(jsonParticipants);
//                    CurrentCourse translateCurrentCourse = new CurrentCourse();
//
//                    translateCurrentCourse.setParticipants((ArrayList<.>) jsonParticipants);
//                    translateCurrentCourse.setAllure(jsonAllure);
                        Gson gson = new Gson();


                        //     log.info("What is in translateCurrentCourse {}",gson.toJson(translateCurrentCourse));
                           // before return extracted course save it in local database
                           Course courseToSave = new Course();
                           courseToSave.setCourseId(getCourseFromBrute(bruteParticipants).getCourseId());
                           ArrayList<InfosParticipant> participantsll = getCourseFromBrute(bruteParticipants).getParticipants();

                           String element = gson.toJson(
                                   participantsll,
                                new TypeToken<ArrayList<InfosParticipant>>() {}.getType());


                           log.info("element {}",element);
                       // new ArrayList<String>(Arrays.asList(s.split(",")));
                           courseToSave.setParticipants(new ArrayList<InfosParticipant>());
                           this.courseDao.save(courseToSave);
                        return new ResponseEntity<>(bruteParticipants.toString() ,HttpStatus.OK);
                    }

                }

            }



        }catch(Exception ex) {
            ex.printStackTrace();
        }
            return  QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> getPerfomances(Map<String, String> requestMap) {
        try {
            log.info("Inside signup {}",requestMap);
            if(validateRefCourse(requestMap)) {
                String toDay = new SimpleDateFormat("ddMMyyyy").format(new Date());
                String currentUrl="";
                currentUrl = "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/"+toDay+"/"+requestMap.get("R")+"/"+requestMap.get("C")+"/performances-detaillees/pretty";
                URL url = new URL(currentUrl);
                log.info("CurrentUrl {}",currentUrl);
                // https://online.turfinfo.api.pmu.fr/rest/client/1/programme/09052024/R1/C7/participants?specialisation=INTERNET
                //   this.urlParticipants="/"+dateCourse.toISOString().split('-')[2].substring(0,2)+dateCourse.toISOString().split('-')[1]+dateCourse.toISOString().split('-')[0]+"/"+this.refCourse.R+"/"+this.refCourse.C+"/participants?specialisation=INTERNET"
                // "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/09022024/R7/C5/performances-detaillees/pretty"

                // "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/"+toDay+"/"+requestMap.get("R")+"/"+requestMap.get("C")+"/performances-detaillees/pretty";
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

                    JsonElement bruteParticipants = dataObject.getAsJsonArray();
                    log.info("bruteParticipants {}",bruteParticipants);

                    Type listType = new TypeToken<List<Participant>>() {}.getType();
                    Type allureType = new TypeToken<String>() {}.getType();
                    // List<InfosParticipant> jsonParticipants = (List<InfosParticipant>) bruteParticipants;
                    String jsonAllure = new Gson().fromJson(dataObject.get("allure"),allureType);

//                    System.out.println(jsonParticipants);
//                    CurrentCourse translateCurrentCourse = new CurrentCourse();
//
//                    translateCurrentCourse.setParticipants((ArrayList<Participant>) jsonParticipants);
//                    translateCurrentCourse.setAllure(jsonAllure);
                    Gson gson = new Gson();


                    //     log.info("What is in translateCurrentCourse {}",gson.toJson(translateCurrentCourse));

                    return new ResponseEntity<>(bruteParticipants.toString() ,HttpStatus.OK);
                }
            }


        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return  QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateRefCourse(Map<String,String> requestMap) {
        return requestMap.containsKey("R") && requestMap.containsKey("C") && requestMap.containsKey("specialite");

    }

    private Course getCourseFromBrute(JsonElement bruteParticipants ) {
        Gson gson = new Gson();
      //  Arrays<Participant> localParticipants  = gson.fromJson(EntityUtils.toString(response.getEntity()), DataResponse.class);
        Course course = new Course();
        Type listType = new TypeToken<List<InfosParticipant>>() {}.getType();
        course.setCourseId("R1C4-15022024");
        log.info("prueParticipant inside getCourseFromBrute {}",bruteParticipants);
        String[] splitedString = bruteParticipants.toString().split("(?<=],)",2);
        log.info("splitedString[1] {}",splitedString[0].substring(16));
        String segondSplitedString = splitedString[0].substring(16).replaceFirst(".$", "");
        //log.info("segondSplitedString {}",segondSplitedString);
        ArrayList<InfosParticipant> locatParticipants = gson.fromJson(segondSplitedString, listType);
        log.info("locatParticipants {}",locatParticipants);
        course.setParticipants(locatParticipants);
        log.info("course {}",course);


        return course;
                //
                //bruteParticipants;
    }
}
