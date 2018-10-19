package com.example.elab_yang.treadmill;


public class CardItem {
    private String date;
    private String time;
    private String ei;
    private String speed;
    private String distance;
    private String bpm;
    private String kcal;

    public CardItem(String date, String time, String ei, String speed, String distance, String bpm, String kcal) {
        this.date = date;
        this.time = time;
        this.ei = ei;
        this.speed = speed;
        this.distance = distance;
        this.bpm = bpm;
        this.kcal = kcal;
    }

    public String getEi() {
        return ei;
    }

    public void setEi(String ei) {
        this.ei = ei;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getBpm() {
        return bpm;
    }

    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }
}