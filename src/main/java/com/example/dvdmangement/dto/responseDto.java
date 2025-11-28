package com.example.dvdmangement.dto;

public class responseDto {
    private final int id;
    private final String title;
    private final int audience;
    private final String date;
    private final int grade;
    private final boolean available;

    public responseDto(int id, String title, int audience, String date, int grade, boolean available) {
        super();
        this.id = id;
        this.title = title;
        this.audience = audience;
        this.date = date;
        this.grade = grade;
        this.available = available;
    }

    /*
    final은 setter가 필요없음!
     */
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getAudience() {
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
