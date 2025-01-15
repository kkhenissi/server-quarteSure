package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentProgrammeStringFormat;
import com.fdc.quartesure.quartesurebackend.POJO.PerformanceStringFormat;
import com.fdc.quartesure.quartesurebackend.dao.CurrentProgrammeStringFormatDao;
import com.fdc.quartesure.quartesurebackend.service.CurrentProgrammeService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.google.gson.Gson;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
@Service
public class CurrentProgrammeServiceImpl implements CurrentProgrammeService {


    @Autowired
    CurrentProgrammeStringFormatDao currentProgrammeStringFormatDao;
    @Override
    public ResponseEntity<String> getTodayProgramme(Map<String, String> requestMap) throws IOException {


        try {
            log.info("Inside getTodayProgramme {}",requestMap);
  //          if(validateRefCourse(requestMap)) {
            //    String aujourdhui = new SimpleDateFormat("ddMMyyyy").format(new Date());
                // "07/07/2024"
                String currentUrl="";
                currentUrl = "https://online.turfinfo.api.pmu.fr/rest/client/61/programme/"+requestMap.get("date")+"?meteo=true&specialisation=INTERNET";
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
//                    Type listType = new TypeToken<List<Participant>>() {
//                    }.getType();
//                    Type allureType = new TypeToken<String>() {
//                    }.getType();
//                    // List<InfosParticipant> jsonParticipants = (List<InfosParticipant>) bruteParticipants;
//                    String jsonAllure = new Gson().fromJson(dataObject.get("allure"), allureType);

                    //    System.out.println(informationRetrived);
//                    try {
//                        JsonNode jsonTree = new ObjectMapper().readTree(String.valueOf(dataObject));
//                        System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
//
//                        CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
//                        System.out.println(csvSchemaBuilder);
//                        JsonNode firstObject = jsonTree.elements().next();
//                        csvSchemaBuilder.addColumn(String.valueOf(firstObject));
//
//                        firstObject.fieldNames().forEachRemaining(fieldName -> {
//                            System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
//                            System.out.println(firstObject);
//                            System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
//                         //   csvSchemaBuilder.addColumn(fieldName);
//                        });
//                        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
//                        CsvMapper csvMapper = new CsvMapper();
////                        csvMapper.writerFor(JsonNode.class)
////                                .with(csvSchema)
////                                .writeValue(new File("src/main/resources/lmsget.csv"), jsonTree);
//
//                    } catch (Exception ex) {
//                        throw ex;
//                    }



//                    System.out.println(jsonParticipants);
//                    CurrentCourse translateCurrentCourse = new CurrentCourse();
//
//                    translateCurrentCourse.setParticipants((ArrayList<Participant>) jsonParticipants);
//                    translateCurrentCourse.setAllure(jsonAllure);
                    Gson gson = new Gson();


                    //     log.info("What is in translateCurrentCourse {}",gson.toJson(translateCurrentCourse));

                    return new ResponseEntity<>(bruteProgramme.toString() , HttpStatus.OK);
                }
 //           }


        }catch(Exception ex) {
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
        return null;
    }

    @Override
    public Optional<PerformanceStringFormat> getYesterdayProgrammeFromLocalDB(Map<String, String> requestMap) throws IOException {
        return Optional.empty();
    }
}
