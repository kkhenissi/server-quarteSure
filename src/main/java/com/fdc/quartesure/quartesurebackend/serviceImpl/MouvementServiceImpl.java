package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.Mouvement;
import com.fdc.quartesure.quartesurebackend.dao.MouvementDao;
import com.fdc.quartesure.quartesurebackend.service.MouvementService;
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
import java.util.*;

@Slf4j
@Service
public class MouvementServiceImpl implements MouvementService {
    @Autowired
    private MouvementDao mouvementDao;



    @Override
    public ResponseEntity<List<String>> getAll() throws IOException {


        try {
       //     log.info("Inside getTodayProgramme {}",requestMap);
            //          if(validateRefCourse(requestMap)) {
                String now = new SimpleDateFormat("ddMMyyyy:mm:ss").format(new Date());
            // "07/07/2024"
            String currentUrl="";
            currentUrl = "https://connect.awl.pmu.fr/account/client/61/mouvements?origin=gcmv&offset=1&nbElements=50";
            String mouvementStringFormatId = "MVT-"+now;
            System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
         //   System.out.println(currentProgrammeStringFormatId);
         //   System.out.println(currentProgrammeStringFormatId);
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
                Mouvement mouvementToSave = new Mouvement();
                mouvementToSave.setId(mouvementStringFormatId);
                mouvementToSave.setContent(bruteProgramme.toString());
                log.info("programmeToSave {}", mouvementToSave);
                mouvementDao.save(mouvementToSave);
                log.info("mouvementToSave {}", mouvementToSave);

                Gson gson = new Gson();


                //     log.info("What is in translateCurrentCourse {}",gson.toJson(translateCurrentCourse));

                return new ResponseEntity<List<String>>((List<String>) mouvementToSave, HttpStatus.OK);
            }
            //           }


        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
                // QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);





     //   return (ResponseEntity<List<MouvementWrapper>>) mouvementDao.getAllMouvement();
    }
}
