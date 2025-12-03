package com.example.dvdmangement.dto;

public class rentalInfoDTO {
    private final String userName;
    private final int userAge;
    private final int userId;
    private final String title;
    private final String rentalDate;
    private final int movieId;

    public rentalInfoDTO(String userName, int userAge, int userId,String title,String rentalDate, int movieId) {
        this.userName = userName;
        this.userAge = userAge;
        this.title = title;
        this.rentalDate = rentalDate;
        this.movieId = movieId;
        this.userId = userId;
    }
    public String getUserName() {return userName;}
    public int getUserAge() {
        return userAge;
    }
    public String getTitle() {return title;}
    public String getRentalDate() {
        return rentalDate;
    }
    public int getMovieId() {
        return movieId;
    }
    public int getUserId() {
        return userId;
    }
}
