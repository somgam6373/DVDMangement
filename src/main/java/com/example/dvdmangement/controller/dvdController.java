package com.example.dvdmangement.controller;

import com.example.dvdmangement.dto.ResponseDTO;
import com.example.dvdmangement.service.dvdService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class dvdController {
    private final dvdService dvdService;

    public dvdController(dvdService dvdService) {
        this.dvdService = dvdService;
    }

    @GetMapping("/dvdData")
        public List<ResponseDTO> getAllDvds(){
            return dvdService.getAllDvds();
        }
}
