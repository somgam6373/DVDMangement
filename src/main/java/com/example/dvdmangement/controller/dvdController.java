package com.example.dvdmangement.controller;

import com.example.dvdmangement.dto.RequestDTO;
import com.example.dvdmangement.dto.ResponseDTO;
import com.example.dvdmangement.dto.rentalInfoDTO;
import com.example.dvdmangement.service.dvdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
public class dvdController {
    private final dvdService dvdService;

    public dvdController(dvdService dvdService) {
        this.dvdService = dvdService;
    }

    //movie 데이터 호출
    @GetMapping("/dvdData")
    public List<ResponseDTO> getAllDvds(){
        return dvdService.getAllDvds();
    }


    //대여된 DVD 데이터 호출
    @GetMapping("/rentData")
    public List<rentalInfoDTO> getAllRents(){
        return dvdService.getAllRents();
    }


    //DVD 대여
    @PostMapping("/rentMovie")
        public String rentMovie(@RequestBody RequestDTO request){

        String name = request.getUserName();
        Integer age = request.getUserAge();
        Integer movieId = request.getMovieId();
        String movieTitle = request.getMovieTitle();

        try {
            dvdService.rentMovie(name, age, movieId, movieTitle);
            return "{\"success\": true}";
        }
        catch (IllegalStateException  e){
            return "{\"success\": false}";
        }
    }

    @PostMapping("/returnMovie")
        public String returnMovie(@RequestBody rentalInfoDTO rentalinfo){

        int movieId = rentalinfo.getMovieId();
        int userId = rentalinfo.getMovieId();

        try {
            dvdService.returnMovie(movieId, userId);
            return "{\"success\": true}";
        }
        catch (IllegalStateException e){
            return "{\"success\": false}";
        }

    }
}
