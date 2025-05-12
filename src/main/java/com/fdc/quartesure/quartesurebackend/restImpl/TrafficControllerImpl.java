package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.constants.QuarteSureConstants;
import com.fdc.quartesure.quartesurebackend.rest.TrafficController;
import com.fdc.quartesure.quartesurebackend.service.TrafficService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.fdc.quartesure.quartesurebackend.wrapper.TrafficStatsWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
public class TrafficControllerImpl implements TrafficController {
    private final TrafficService trafficService;

    public TrafficControllerImpl(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @Override
    public ResponseEntity redirect(@RequestBody Map<String, String> requestMap, Map<String, String> response) throws IOException {
      //  String ip = requestMap.get("ip");
        try {
            trafficService.logTraffic(requestMap);
            response.getOrDefault("",requestMap.get("url"));
            log.info("Inside trafficController {}",requestMap);

            return QuarteSureUtils.getResponseEntity("Succesfully Redirected.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public List<TrafficStatsWrapper> getStatsGroupedByDate() {
        return trafficService.getStatsGroupedByDate();
                //findAllTrafficLogs();
    }
    @Override
    public List<TrafficStatsWrapper> getStatsGroupedByWeek() {
        return trafficService.getStatsGroupedByWeek();
        //findAllTrafficLogs();
    }
    @Override
    public List<TrafficStatsWrapper> getStatsGroupedByMonth() {
        return trafficService.getStatsGroupedByMonth();
        //findAllTrafficLogs();
    }
}
