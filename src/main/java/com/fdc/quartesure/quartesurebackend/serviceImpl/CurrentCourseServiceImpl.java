package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourseStringFormat;
import com.fdc.quartesure.quartesurebackend.POJO.Participant;
import com.fdc.quartesure.quartesurebackend.POJO.PerformanceStringFormat;
import com.fdc.quartesure.quartesurebackend.dao.CurrentCourseStringFormatDao;
import com.fdc.quartesure.quartesurebackend.dao.PerformanceStringFormatDao;
import com.fdc.quartesure.quartesurebackend.service.CurrentCourseService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class CurrentCourseServiceImpl implements CurrentCourseService {

    @Autowired
    CurrentCourseStringFormatDao currentCourseStringFormatDao;

    @Autowired
    PerformanceStringFormatDao performanceStringFormatDao;
    @Override
    public ResponseEntity<String> getCourse(Map<String, String> requestMap) throws IOException {

        try {
            log.info("Inside signup {}",requestMap);
            if(validateRefCourse(requestMap)) {
                String aujourdhui = new SimpleDateFormat("ddMMyyyy").format(new Date());
                // "07/07/2024"
                String currentUrl="";
                currentUrl = "https://online.turfinfo.api.pmu.fr/rest/client/1/programme/"+requestMap.get("date")+"/"+requestMap.get("R")+"/"+requestMap.get("C")+"/participants?specialisation=INTERNET";
                String currentCurseStringFormatId = requestMap.get("date")+"-"+requestMap.get("R")+"-"+requestMap.get("C");
                System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                System.out.println(currentCurseStringFormatId);
                System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                URL url = new URL(currentUrl);
                log.info("CurrentUrl {}",currentUrl);



                // https://online.turfinfo.api.pmu.fr/rest/client/1/programme/09052024/R1/C7/participants?specialisation=INTERNET
                //   this.urlParticipants="/"+dateCourse.toISOString().split('-')[2].substring(0,2)+dateCourse.toISOString().split('-')[1]+dateCourse.toISOString().split('-')[0]+"/"+this.refCourse.R+"/"+this.refCourse.C+"/participants?specialisation=INTERNET"
                // "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/09022024/R7/C5/performances-detaillees/pretty"

                // "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/"+aujourdhui+"/"+requestMap.get("R")+"/"+requestMap.get("C")+"/performances-detaillees/pretty";
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
                    while (scanner.hasNext()) {
                        informationRetrived.append(scanner.nextLine());
                    }
                    // close the scanner
                    scanner.close();

                    JsonObject dataObject = new JsonParser().parse((informationRetrived.toString())).getAsJsonObject();
                    JsonElement bruteParticipants = dataObject;
                    CurrentCourseStringFormat courseToSave = new CurrentCourseStringFormat();
                    courseToSave.setId(currentCurseStringFormatId);
                    courseToSave.setContent(bruteParticipants.toString());
                    log.info("courseToSave {}", courseToSave);
                    currentCourseStringFormatDao.save(courseToSave);
                    log.info("bruteParticipants {}", bruteParticipants);

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

    @Override
    public Optional<CurrentCourseStringFormat> getCourseFromLocalDB(Map<String, String> requestMap) throws IOException {
        String aujourdhui = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String currentCurseStringFormatId = requestMap.get("date")+"-"+requestMap.get("R")+"-"+requestMap.get("C");

/*        Optional strinTolog = this.currentCourseStringFormatDao.findById(o);
        log.info("strinTolog {}", strinTolog);*/
        return this.currentCourseStringFormatDao.findById(currentCurseStringFormatId);
    }

    @Override
    public ResponseEntity<String> getPerfomances(Map<String, String> requestMap) {
        try {
            log.info("Inside signup {}",requestMap);
            if(validateRefCourse(requestMap)) {
                String aujourdhui = new SimpleDateFormat("ddMMyyyy").format(new Date());
                // "07/05/2024"
                String currentUrl="";
                currentUrl = "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/"+requestMap.get("date")+"/"+requestMap.get("R")+"/"+requestMap.get("C");
                log.info("What is in performanceUrl , {}",currentUrl);
               URL url = new URL(currentUrl+"/performances-detaillees/pretty");
                log.info("CurrentUrl {}",currentUrl);
                String performanceStringFrmatId = requestMap.get("date")+"-"+requestMap.get("R")+"-"+requestMap.get("C");

                // https://online.turfinfo.api.pmu.fr/rest/client/1/programme/09052024/R1/C7/participants?specialisation=INTERNET
                //   this.urlParticipants="/"+dateCourse.toISOString().split('-')[2].substring(0,2)+dateCourse.toISOString().split('-')[1]+dateCourse.toISOString().split('-')[0]+"/"+this.refCourse.R+"/"+this.refCourse.C+"/participants?specialisation=INTERNET"
                // "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/09022024/R7/C5/performances-detaillees/pretty"

                // "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/"+aujourdhui+"/"+requestMap.get("R")+"/"+requestMap.get("C")+"/performances-detaillees/pretty";
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


              //      JsonElement bruteParticipants = dataObject;
                    PerformanceStringFormat performanceToSave = new PerformanceStringFormat();
                    performanceToSave.setId(performanceStringFrmatId);
                    performanceToSave.setContent(bruteParticipants.toString());
                    performanceStringFormatDao.save(performanceToSave);


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

    @Override
    public Optional<PerformanceStringFormat> getPerformanceFromLocalDB(Map<String, String> requestMap) throws IOException {
        String aujourdhui = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String performanceStringFrmatId = requestMap.get("date")+"-"+requestMap.get("R")+"-"+requestMap.get("C");
        return this.performanceStringFormatDao.findById(performanceStringFrmatId);
    }

    @Override
    public ResponseEntity<String> getResultatCourse(Map<String, String> requestMap) {
        try {
            log.info("Inside signup {}",requestMap);
            if(validateRefCourse(requestMap)) {
                String aujourdhui = new SimpleDateFormat("ddMMyyyy").format(new Date());
                // "07/05/2024"
                String currentUrl="";
                currentUrl = "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/"+requestMap.get("date")+"/"+requestMap.get("R")+"/"+"?specialisation=INTERNET";
                URL url = new URL(currentUrl);
                log.info("CurrentUrl {}",currentUrl);
             //   String performanceStringFrmatId = requestMap.get("date")+"-"+requestMap.get("R")+"-"+requestMap.get("C");

                // https://online.turfinfo.api.pmu.fr/rest/client/1/programme/09052024/R1/C7/participants?specialisation=INTERNET
                //   this.urlParticipants="/"+dateCourse.toISOString().split('-')[2].substring(0,2)+dateCourse.toISOString().split('-')[1]+dateCourse.toISOString().split('-')[0]+"/"+this.refCourse.R+"/"+this.refCourse.C+"/participants?specialisation=INTERNET"
                // "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/09022024/R7/C5/performances-detaillees/pretty"

                // "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/"+aujourdhui+"/"+requestMap.get("R")+"/"+requestMap.get("C")+"/performances-detaillees/pretty";
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

                    JsonElement bruteResultats = dataObject;
                    log.info("bruteResultats {}",bruteResultats);
                    Type listType = new TypeToken<List<Participant>>() {}.getType();
                    Type allureType = new TypeToken<String>() {}.getType();
                    // List<InfosParticipant> jsonParticipants = (List<InfosParticipant>) bruteResultats;
                    String jsonAllure = new Gson().fromJson(dataObject.get("allure"),allureType);


                    //      JsonElement bruteResultats = dataObject;
               //     PerformanceStringFormat performanceToSave = new PerformanceStringFormat();
               //     performanceToSave.setId(performanceStringFrmatId);
                //    performanceToSave.setContent(bruteResultats.toString());
                 //   performanceStringFormatDao.save(performanceToSave);


                    Gson gson = new Gson();


                    //     log.info("What is in translateCurrentCourse {}",gson.toJson(translateCurrentCourse));

                    return new ResponseEntity<>(bruteResultats.toString() ,HttpStatus.OK);
                }
            }


        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return  QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public JsonNode getCourseByOrder(Map<String, String> requestMap, int numOrderTarget) {
        return null;
    }

    @Override
    public ResponseEntity<String> getOrdreArriveeByNumOrdre(Map<String, String> requestMap, int numOrdreTarget) {
        log.info("🎯 NumOrdre cible: {}", numOrdreTarget);
        try {
            log.info("📦 Contenu requestMap: {}", requestMap);

            if (validateRefCourse(requestMap)) {
                String currentUrl = String.format(
                        "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/%s/%s?specialisation=INTERNET",
                        requestMap.get("date"), requestMap.get("R")
                );

                log.info("🌍 URL appelée: {}", currentUrl);

                HttpURLConnection conn = (HttpURLConnection) new URL(currentUrl).openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("HttpResponseCode:" + conn.getResponseCode());
                }

                // Lecture du contenu
                StringBuilder response = new StringBuilder();
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    while (scanner.hasNext()) response.append(scanner.nextLine());
                }

                JsonObject root = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonArray courses = root.getAsJsonArray("courses");

                if (courses == null) {
                    throw new IllegalArgumentException("Champ 'courses' manquant ou invalide dans la réponse PMU");
                }

                for (JsonElement courseElem : courses) {
                    JsonObject course = courseElem.getAsJsonObject();
                    int numOrdre = course.get("numOrdre").getAsInt();

                    if (numOrdre == numOrdreTarget) {
                        JsonElement ordreArrivee = course.get("ordreArrivee");
                        if (ordreArrivee == null || ordreArrivee.isJsonNull()) {
                            throw new IllegalStateException("Champ 'ordreArrivee' manquant pour numOrdre=" + numOrdreTarget);
                        }

                        log.info("✅ Ordre d'arrivée trouvé: {}", ordreArrivee);
//                        return ResponseEntity.ok()
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .body(jsonString);

                        return new ResponseEntity<>(ordreArrivee.toString(), HttpStatus.OK);
                    }
                }

                throw new IllegalArgumentException("Aucune course trouvée pour numOrdre=" + numOrdreTarget);
            }

        } catch (Exception ex) {
            log.error("❌ Erreur lors de l’extraction de ordreArrivee pour numOrdre={}", numOrdreTarget, ex);
        }

        return QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateRefCourse(Map<String,String> requestMap) {
        return requestMap.containsKey("R") && requestMap.containsKey("C") && requestMap.containsKey("specialite");

    }
}
