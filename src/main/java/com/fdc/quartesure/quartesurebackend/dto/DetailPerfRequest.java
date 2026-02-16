package com.fdc.quartesure.quartesurebackend.dto;

import lombok.Data;

import java.util.Map;
@Data
public class DetailPerfRequest {
    private RefCourse refCourse;
    private String nomCheval;
    private Map<String, Object> metaDa;
}
