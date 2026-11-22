package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentProgrammeStringFormat;
import com.fdc.quartesure.quartesurebackend.dao.CurrentProgrammeStringFormatDao;
import com.fdc.quartesure.quartesurebackend.service.CurrentProgrammeService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
@Service
public class CurrentProgrammeServiceImpl implements CurrentProgrammeService {


    @Autowired
    CurrentProgrammeStringFormatDao currentProgrammeStringFormatDao;

    /**
     * Pénétromètre dans la réponse PMU d'une réunion : il est porté par chaque course (courses[n].penetrometre). On prend celui
     * de la course demandée (numOrdre ou numExterne = numéro de C), sinon celui de la première course qui en a un, sinon
     * un éventuel pénétromètre à la racine.
     */
    private static JsonElement findPenetrometre(JsonObject reunion, String courseRef) {
        int wanted = (courseRef != null && courseRef.matches("C\\d{1,2}")) ? Integer.parseInt(courseRef.substring(1)) : -1;
        JsonElement fallback = null;
        if (reunion.has("courses") && reunion.get("courses").isJsonArray()) {
            JsonArray courses = reunion.getAsJsonArray("courses");
            for (JsonElement element : courses) {
                if (!element.isJsonObject()) continue;
                JsonObject course = element.getAsJsonObject();
                JsonElement pene = course.get("penetrometre");
                if (pene == null || !pene.isJsonObject()) continue;
                if (fallback == null) fallback = pene;
                int ordre = course.has("numOrdre") && !course.get("numOrdre").isJsonNull() ? course.get("numOrdre").getAsInt() : -1;
                int externe = course.has("numExterne") && !course.get("numExterne").isJsonNull() ? course.get("numExterne").getAsInt() : -1;
                if (ordre == wanted || externe == wanted) return pene;
            }
        }
        if (fallback != null) return fallback;
        return reunion.has("penetrometre") ? reunion.get("penetrometre") : null;
    }

    /**
     * Pénétromètre (état du terrain) de la réunion. Le front ne peut pas appeler PMU directement (« Invalid CORS request »),
     * il passe par ici. Renvoie {} tant que la mesure n'est pas renseignée.
     */
    @Override
    public ResponseEntity<String> getPenetrometre(Map<String, String> requestMap) {
        try {
            String date = requestMap.get("date");
            String reunion = requestMap.get("R");
            if (date == null || reunion == null || !date.matches("\\d{8}") || !reunion.matches("R\\d{1,2}")) {
                return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
            }
            String currentUrl = "https://online.turfinfo.api.pmu.fr/rest/client/1/programme/" + date + "/" + reunion + "?specialisation=INTERNET";
            HttpURLConnection conn = (HttpURLConnection) new URL(currentUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() != 200) {
                return new ResponseEntity<>("{}", HttpStatus.OK);
            }
            StringBuilder response = new StringBuilder();
            try (Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8")) {
                while (scanner.hasNext()) response.append(scanner.nextLine());
            }
            JsonElement pene = findPenetrometre(JsonParser.parseString(response.toString()).getAsJsonObject(), requestMap.get("C"));
            if (pene == null || !pene.isJsonObject()) {
                return new ResponseEntity<>("{}", HttpStatus.OK);
            }
            JsonObject out = new JsonObject();
            for (String key : new String[]{"intitule", "valeurMesure", "heureMesure"}) {
                JsonElement value = pene.getAsJsonObject().get(key);
                if (value != null && !value.isJsonNull()) {
                    out.addProperty(key, value.getAsString());
                }
            }
            return new ResponseEntity<>(out.toString(), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Pénétromètre indisponible", ex);
        }
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> getTodayProgramme(Map<String, String> requestMap) throws IOException {


        try {
            log.info("Inside getTodayProgramme {}",requestMap);
  //          if(validateRefCourse(requestMap)) {
            //    String aujourdhui = new SimpleDateFormat("ddMMyyyy").format(new Date());
                // "07/07/2024"
                String currentUrl="";
                currentUrl = "https://online.turfinfo.api.pmu.fr/rest/client/1/programme/"+requestMap.get("date")+"?meteo=true&specialisation=INTERNET";
                String currentProgrammeStringFormatId = "PRG-"+requestMap.get("date");
                System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                System.out.println(currentProgrammeStringFormatId);
                System.out.println(currentProgrammeStringFormatId);
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

                    JsonElement bruteProgramme = dataObject;
                    CurrentProgrammeStringFormat programmeToSave = new CurrentProgrammeStringFormat();
                    programmeToSave.setId(currentProgrammeStringFormatId);
                    programmeToSave.setContent(bruteProgramme.toString());
                    log.info("programmeToSave {}", programmeToSave);
                    currentProgrammeStringFormatDao.save(programmeToSave);
                    log.info("bruteParticipants {}", bruteProgramme);

                    Gson gson = new Gson();


                    //     log.info("What is in translateCurrentCourse {}",gson.toJson(translateCurrentCourse));

                    return new ResponseEntity<>(bruteProgramme.toString() , HttpStatus.OK);
                }
 //           }


        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return  QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);



    }

    @Override
    public Optional<CurrentProgrammeStringFormat> getTodayProgrammeFromLocalDB(Map<String, String> requestMap) throws IOException {
     //   String aujourdhui = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String currentProgrammeStringFormatId = "PRG-"+requestMap.get("date");

/*        Optional strinTolog = this.currentCourseStringFormatDao.findById(o);
        log.info("strinTolog {}", strinTolog);*/
        return this.currentProgrammeStringFormatDao.findById(currentProgrammeStringFormatId);
    }

    @Override
    public ResponseEntity<String> getYesterdayProgramme(Map<String, String> requestMap) {
        try {
            log.info("Inside getTodayProgramme {}",requestMap);
            //          if(validateRefCourse(requestMap)) {
            //    String aujourdhui = new SimpleDateFormat("ddMMyyyy").format(new Date());
            // "07/07/2024"
            String currentUrl="";
            currentUrl = "https://online.turfinfo.api.pmu.fr/rest/client/1/programme/"+requestMap.get("date")+"?meteo=true&specialisation=INTERNET";
            String currentProgrammeStringFormatId = "PRG-"+requestMap.get("date");
            System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
            System.out.println(currentProgrammeStringFormatId);
            System.out.println(currentProgrammeStringFormatId);
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

                JsonElement bruteProgramme = dataObject;
                CurrentProgrammeStringFormat programmeToSave = new CurrentProgrammeStringFormat();
                programmeToSave.setId(currentProgrammeStringFormatId);
                programmeToSave.setContent(bruteProgramme.toString());
                log.info("programmeToSave {}", programmeToSave);
                currentProgrammeStringFormatDao.save(programmeToSave);
                log.info("bruteParticipants {}", bruteProgramme);

                Gson gson = new Gson();


                //     log.info("What is in translateCurrentCourse {}",gson.toJson(translateCurrentCourse));

                return new ResponseEntity<>(bruteProgramme.toString() , HttpStatus.OK);
            }
            //           }


        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return  QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Optional<CurrentProgrammeStringFormat> getYesterdayProgrammeFromLocalDB(Map<String, String> requestMap) throws IOException {
        String currentProgrammeStringFormatId = "PRG-"+requestMap.get("date");
        return this.currentProgrammeStringFormatDao.findById(currentProgrammeStringFormatId);
    }
}
