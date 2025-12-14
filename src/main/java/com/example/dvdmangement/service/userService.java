package com.example.dvdmangement.service;

import com.example.dvdmangement.dao.UserDao;
import com.example.dvdmangement.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class userService {

    private final UserDao userDao;

    public userService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void signup(UserDTO user) {
        System.out.println(user);
        UserDTO existing = userDao.findByUsername(user.getId());
        if (existing != null) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        userDao.save(user);
    }

    public UserDTO login(String Id, String password) {
        UserDTO user = userDao.findByUsername(Id);

        if (user == null) {
            throw new IllegalStateException("아이디가 존재하지 않습니다.");
        }

        if (!user.getPassword().equals(password)) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    public UserDTO findByUsername(String id) {
        return userDao.findByUsername(id);
    }
}
