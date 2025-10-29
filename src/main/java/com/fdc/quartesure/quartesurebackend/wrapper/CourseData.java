package com.fdc.quartesure.quartesurebackend.wrapper;

import lombok.Data;

@Data
public class CourseData {
    private int numPmu;
    private double speedChv;
    private double poids;
    private double handicapPoids;
    private int placeCorde;
    private int corde;
    private int distance;
    private int currentDistance;
    private String currentEtatTerrain;
    private int positionArrivee;
    private double predictedSpeed;

    // Getters / Setters
//    public int getNumPmu() { return numPmu; }
//    public void setNumPmu(int numPmu) { this.numPmu = numPmu; }
//
//    public double getSpeedChv() { return speedChv; }
//    public void setSpeedChv(double speedChv) { this.speedChv = speedChv; }
//
//    public double getPoids() { return poids; }
//    public void setPoids(double poids) { this.poids = poids; }
//
//    public int getCorde() { return corde; }
//    public void setCorde(int corde) { this.corde = corde; }
//
//    public int getPositionArrivee() { return positionArrivee; }
//    public void setPositionArrivee(int positionArrivee) { this.positionArrivee = positionArrivee; }
//
//    public double getPredictedSpeed() { return predictedSpeed; }
//    public void setPredictedSpeed(double predictedSpeed) { this.predictedSpeed = predictedSpeed; }
}