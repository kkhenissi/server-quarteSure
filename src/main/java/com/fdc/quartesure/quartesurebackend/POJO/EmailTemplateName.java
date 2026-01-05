package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account"),
    RESET_PASSWORD("reset_password"),
    SIMPLE_MESSAGE("simple-message");
    private final  String name;




}
