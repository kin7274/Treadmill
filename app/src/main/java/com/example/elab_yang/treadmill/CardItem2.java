package com.example.elab_yang.treadmill;


public class CardItem2 {
    private String user_code;
    private String date;
    private String time;
    private String distance;
    private String speed;
    private String bpm;

    public CardItem2(String user_code, String date, String time, String distance, String speed, String bpm) {
        this.user_code = user_code;
        this.date = date;
        this.time = time;
        this.distance = distance;
        this.speed = speed;
        this.bpm = bpm;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getBpm() {
        return bpm;
    }

    public void setBpm(String bpm) {
        this.bpm = bpm;
    }
}