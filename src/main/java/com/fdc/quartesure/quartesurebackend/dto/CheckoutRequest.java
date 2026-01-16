package com.fdc.quartesure.quartesurebackend.dto;

public class CheckoutRequest {

    private Integer amount;    // montant choisi
    private String planName;   // nom du plan
    private String userId;     // facultatif si tu veux passer depuis le front

    // Constructeurs
    public CheckoutRequest() {}

    public CheckoutRequest(Integer amount, String planName) {
        this.amount = amount;
        this.planName = planName;
    }

    // Getters et Setters
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
