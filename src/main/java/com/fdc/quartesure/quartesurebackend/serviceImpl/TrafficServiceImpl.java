package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.TrafficLog;
import com.fdc.quartesure.quartesurebackend.constants.QuarteSureConstants;
import com.fdc.quartesure.quartesurebackend.dao.TrafficLogRepository;
import com.fdc.quartesure.quartesurebackend.service.TrafficService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.fdc.quartesure.quartesurebackend.wrapper.TrafficStatsWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<TrafficLog> findAllTrafficLogs() {
        return repository.findAllTrafficLogs();
    }
    @Override
    public List<TrafficStatsWrapper> getStatsGroupedByDate() {
        List<TrafficLog> logs = repository.findAllTrafficLogs();

        return logs.stream()
                .collect(Collectors.groupingBy(
                        log -> LocalDate.from(log.getTimestamp()), // group by date only
                        Collectors.groupingBy(
                                TrafficLog::getTargetUrl,
                                Collectors.counting()
                        )
                ))
                .entrySet().stream()
                .flatMap(dateEntry -> dateEntry.getValue().entrySet().stream()
                        .map(urlEntry -> new TrafficStatsWrapper(
                                urlEntry.getKey(),  // url
                                dateEntry.getKey(), // date
                                urlEntry.getValue() // count
                        ))
                )
                .collect(Collectors.toList());
    }
}
