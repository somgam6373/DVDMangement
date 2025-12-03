package com.example.dvdmangement.controller;

import com.example.dvdmangement.JwtUtil;
import com.example.dvdmangement.dto.UserDTO;
import com.example.dvdmangement.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class userController {

    @Autowired
    private userService userService;

    // 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody UserDTO user) {
        userService.signup(user);
        return "회원가입이 완료되었습니다.";
    }

    // 로그인 (✅ JWT 토큰 반환)
    @PostMapping("/login")
    public String login(@RequestBody UserDTO loginReq) {
        // 아이디/비번 검증
        UserDTO user = userService.login(loginReq.getUserId(), loginReq.getPassword());

        return JwtUtil.createToken(user.getUserId());
    }
}
