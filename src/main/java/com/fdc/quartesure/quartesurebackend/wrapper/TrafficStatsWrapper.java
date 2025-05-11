package com.fdc.quartesure.quartesurebackend.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor

public class TrafficStatsWrapper {
    private String url;
    private LocalDate date;
    private long count;

    public TrafficStatsWrapper(String url, LocalDate date, Long count) {
        this.url = url;
        this.date = date;
        this.count = count;
    }
}
