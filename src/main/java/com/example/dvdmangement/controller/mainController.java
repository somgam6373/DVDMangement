package com.example.dvdmangement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {
    @GetMapping("/")
    public String home() {
        return "main"; // templates/main.html 렌더링
    }
}