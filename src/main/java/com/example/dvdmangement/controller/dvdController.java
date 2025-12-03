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


    // DVD 대여 (JWT 기반)
    @PostMapping("/rentMovie")
    public String rentMovie(@RequestBody RequestDTO request,
                            HttpServletRequest httpRequest) {

        // ✅ JwtInterceptor에서 넣어둔 username 꺼내기
        String username = (String) httpRequest.getAttribute("username");
        if (username == null) {
            // 인터셉터에서 대부분 걸러지겠지만 혹시 모르니 한 번 더 체크
            return "{\"success\": false, \"message\": \"토큰이 없습니다.\"}";
        }

        // ✅ username으로 유저 정보 조회
        UserDTO user = userService.findByUsername(username);
        if (user == null) {
            return "{\"success\": false, \"message\": \"유저를 찾을 수 없습니다.\"}";
        }

        Integer movieId = request.getMovieId();
        String movieTitle = request.getMovieTitle();

        try {
            // ✅ 이제 name/age는 JWT 기반 유저 정보 사용
            dvdService.rentMovie(user.getUsername(), user.getAge(), movieId, movieTitle);
            return "{\"success\": true}";
        } catch (IllegalStateException e) {
            return "{\"success\": false}";
        }
    }

    @PostMapping("/returnMovie")
        public String returnMovie(@RequestBody rentalInfoDTO rentalinfo){

        int movieId = rentalinfo.getMovieId();
        int userId = rentalinfo.getUserId();

        try {
            dvdService.returnMovie(movieId, userId);
            return "{\"success\": true}";
        }
        catch (IllegalStateException e){
            return "{\"success\": false}";
        }

    }
}
