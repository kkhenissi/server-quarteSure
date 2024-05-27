package com.fdc.quartesure.quartesurebackend.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWrapper {

    private Integer id;

    private String firstName;

    private String email;

    private String phoneNumber;

    private String status;
}
