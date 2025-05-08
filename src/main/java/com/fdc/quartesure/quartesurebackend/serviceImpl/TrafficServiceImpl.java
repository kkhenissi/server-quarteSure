package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.TrafficLog;
import com.fdc.quartesure.quartesurebackend.constants.QuarteSureConstants;
import com.fdc.quartesure.quartesurebackend.dao.TrafficLogRepository;
import com.fdc.quartesure.quartesurebackend.service.TrafficService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
@Slf4j
@Service
public class TrafficServiceImpl implements TrafficService {

    private final TrafficLogRepository repository;

    public TrafficServiceImpl(TrafficLogRepository repository) {
        this.repository = repository;
    }


    @Override
    public ResponseEntity<String> logTraffic(Map<String, String> requestMap) {
        try {
            TrafficLog trafficLoglog = new TrafficLog();
            trafficLoglog.setTargetUrl(requestMap.get("url"));
            trafficLoglog.setIpAddress(requestMap.get("ip"));
            trafficLoglog.setTimestamp(LocalDateTime.now());

            repository.save(trafficLoglog);
            log.info("Inside signup {}",trafficLoglog);
            return QuarteSureUtils.getResponseEntity("Succesfully Registred.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
