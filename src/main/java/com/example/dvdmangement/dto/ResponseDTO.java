package com.example.dvdmangement.dto;

public class ResponseDTO {
    private  int id;
    private  String title;
    private  String audience;
    private  String date;
    private  int grade;
    private  boolean available;

    public ResponseDTO(int id, String title, String audience, String date, int grade, boolean available) {
        this.id = id;
        this.title = title;
        this.audience = audience;
        this.date = date;
        this.grade = grade;
        this.available = available;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAudience() {
        return audience;
    }
    public String getDate() {
        return date;
    }
    public int getGrade() {
        return grade;
    }

    public boolean isAvailable() {
        return available;
    }
}
