package com.example.dvdmangement.controller;

import com.example.dvdmangement.dto.RequestDTO;
import com.example.dvdmangement.dto.ResponseDTO;
import com.example.dvdmangement.dto.UserDTO;
import com.example.dvdmangement.dto.rentalInfoDTO;
import com.example.dvdmangement.service.dvdService;
import com.example.dvdmangement.service.userService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
public class dvdController {
    private final dvdService dvdService;
    private final userService userService;

    public dvdController(dvdService dvdService, userService userService) {
        this.dvdService = dvdService;
        this.userService = userService;
    }

    @GetMapping("/dvdData")
    public List<ResponseDTO> getAllDvds(){
        return dvdService.getAllDvds();
    }

    @GetMapping("/getUser")
    public List<UserDTO> getUser(){
        return dvdService.getUser();}

    @PostMapping("/rentData")
    public List<rentalInfoDTO> getAllRents(@RequestBody Map<String, Object> request) {
        int userId = (int) request.get("user_id");
        return dvdService.getAllRents(userId);
    }

        @PostMapping("/rentMovie")
    public String rentMovie(@RequestBody RequestDTO request,
                            HttpServletRequest httpRequest) {

        String id = (String) httpRequest.getAttribute("id");
        if (id == null) {
            return "{\"success\": false, \"message\": \"토큰이 없습니다.\"}";
        }

        UserDTO user = userService.findByUsername(id);
        Integer movieId = request.getMovieId();

        try {
            dvdService.rentMovie(user.getUserid(), movieId);
            return "{\"success\": true, \"message\": \"대여 완료\"}";
        } catch (IllegalStateException e) {
            return "{\"success\": false, \"message\": \"" + e.getMessage() + "\"}";
        }
    }

    @PostMapping("/returnMovie")
    public String returnMovie(@RequestBody Map<String, Integer> request) {

        Integer rentalId = request.get("Id");
        if (rentalId == null) {
            return "{\"success\": false, \"message\": \"Rental ID 없음\"}";
        }

        try {
            dvdService.returnMovie(rentalId);
            return "{\"success\": true}";
        } catch (Exception e) {
            return "{\"success\": false}";
        }
    }
}
