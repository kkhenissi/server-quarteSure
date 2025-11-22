package com.fdc.quartesure.quartesurebackend.wrapper;

import lombok.Data;

@Data
public class RegressionResult {
    private double alpha;
    private double beta;
    private double gamma;
    private double lambda;
    private double r2;

    public RegressionResult(double alpha, double beta, double gamma, double r2) {
      //  , double gamma, double lambda
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
//        this.lambda = lambda;
        this.r2 = r2;
    }

//    public double getAlpha() { return alpha; }
//    public double getBeta() { return beta; }
//    public double getGamma() { return gamma; }
//    public double getR2() { return r2; }
//
//    public void setAlpha(double alpha) { this.alpha = alpha; }
//    public void setBeta(double beta) { this.beta = beta; }
//    public void setGamma(double gamma) { this.gamma = gamma; }
//    public void setR2(double r2) { this.r2 = r2; }
}