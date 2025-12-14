package com.example.dvdmangement.service;

import java.util.List;

import com.example.dvdmangement.dto.RequestDTO;
import com.example.dvdmangement.dto.ResponseDTO;
import com.example.dvdmangement.dao.dvdDao;
import com.example.dvdmangement.dto.UserDTO;
import com.example.dvdmangement.dto.rentalInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class dvdService {
    private dvdDao dao = new dvdDao();

    public List<ResponseDTO> getAllDvds() {

        return dao.findAllDvd();
    }
    public List<UserDTO> getUser(){
        return dao.findAllUser();
    }
    public List<rentalInfoDTO> getAllRents(int userId) {
        return dao.findAllRents(userId);
    }

    //DVD 대여
    public void rentMovie(int userId,int movieId) {

        List<ResponseDTO>dvds = dao.findAllDvd();

        ResponseDTO dvd = null;

        for(ResponseDTO Id : dvds) {
            if(Id.getId() == movieId) {
                dvd = Id;
                break;
            }
        }

        if(dvd == null) {
            throw new IllegalStateException("해당 제목의 영화가 존재하지 않습니다." + dvd);
        }


        dao.rentMovie(userId, movieId);

        System.out.println(
                "[대여 완료] " + userId + "님이 '" + dvd.getTitle()
                        + "'(ID: " + movieId + ")를 대여했습니다."
        );
    }


    public void returnMovie(int rentalId) {
        dao.returnMovie(rentalId);
    }

}