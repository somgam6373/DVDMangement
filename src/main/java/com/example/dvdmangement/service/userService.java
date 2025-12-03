package com.example.dvdmangement.service;

import com.example.dvdmangement.dao.UserDao;
import com.example.dvdmangement.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class userService {

    private final UserDao userDao;

    // 생성자 주입
    public userService(UserDao userDao) {
        this.userDao = userDao;
    }

    // 회원가입
    public void signup(UserDTO user) {
        // 1) 아이디 중복 체크
        UserDTO existing = userDao.findByUsername(user.getUsername());
        if (existing != null) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        // 3) DB 저장
        userDao.save(user);
    }

    // 로그인
    public UserDTO login(String userId, String password) {
        UserDTO user = userDao.findByUsername(userId);

        if (user == null) {
            throw new IllegalStateException("아이디가 존재하지 않습니다.");
        }

        // 비밀번호 검증 (암호화 안 쓴 버전)
        if (!user.getPassword().equals(password)) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    // username으로 유저 정보 조회 (JWT에서 사용)
    public UserDTO findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
