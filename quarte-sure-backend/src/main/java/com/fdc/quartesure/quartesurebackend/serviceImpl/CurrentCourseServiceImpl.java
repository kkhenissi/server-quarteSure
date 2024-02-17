package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourse;
import com.fdc.quartesure.quartesurebackend.POJO.InfosParticipant;
import com.fdc.quartesure.quartesurebackend.service.CurrentCourseService;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
@Service
public class CurrentCourseServiceImpl implements CurrentCourseService {
    @Override
    public ResponseEntity<CurrentCourse> getCourse(Map<String, String> requestMap) {


        try {
            URL url = new URL("https://online.turfinfo.api.pmu.fr/rest/client/61/programme/09022024/R7/C5/performances-detaillees/pretty");
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
                System.out.println(informationRetrived);
                GsonJsonParser parse = new GsonJsonParser();
                List<Object> dataObject = parse.parseList(informationRetrived.toString());
                System.out.println(dataObject);
                return null;
                        //new ResponseEntity<Participant[]>(String.valueOf((Map<String, String>) dataObject),HttpStatus.OK
                //);
            }

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
                // QuarteSureUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
