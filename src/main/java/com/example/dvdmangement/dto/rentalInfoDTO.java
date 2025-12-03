package com.example.dvdmangement.dto;

public class rentalInfoDTO {
    private final String title;
    private final String rentalDate;
    private final int Rental_Id;

    public rentalInfoDTO(String title,String rentalDate, int Rental_Id) {
        this.title = title;
        this.rentalDate = rentalDate;
        this.Rental_Id = Rental_Id;


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
