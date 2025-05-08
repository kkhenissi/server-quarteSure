package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.constants.QuarteSureConstants;
import com.fdc.quartesure.quartesurebackend.rest.TrafficController;
import com.fdc.quartesure.quartesurebackend.service.TrafficService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/traffic")
public class TrafficControllerImpl implements TrafficController {
    private final TrafficService trafficService;

    public TrafficControllerImpl(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @Override
    @PostMapping("/redirect")
    public ResponseEntity redirect(@RequestBody Map<String, String> requestMap, Map<String, String> response) throws IOException {
      //  String ip = requestMap.get("ip");
        try {
            trafficService.logTraffic(requestMap);
            response.getOrDefault("",requestMap.get("url"));
            log.info("Inside trafficController {}",requestMap.get("url"));

            return QuarteSureUtils.getResponseEntity("Succesfully Redirected.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
