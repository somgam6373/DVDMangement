package com.example.dvdmangement.service;

import java.util.List;

import com.example.dvdmangement.dto.RequestDTO;
import com.example.dvdmangement.dto.ResponseDTO;
import com.example.dvdmangement.dao.dvdDao;
import com.example.dvdmangement.dto.rentalInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class dvdService {
    private dvdDao dao = new dvdDao();

    //movie 데이터 호출
    public List<ResponseDTO> getAllDvds() {

        return dao.findAllDvd();
    }

    //대여된 DVD 데이터 호출
    public List<rentalInfoDTO> getAllRents() {
        //Dao 수정시 findAllDvd -> findAllRents로 수정
        return dao.findAllRents();
    }


    //DVD 대여
    public void rentMovie(String name, int age, int movieId, String movieTitle) {

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


        if(dvd.getGrade()>age){
            throw new IllegalStateException("해당 영화 관람 불가 연령입니다. 사용자 나이: " + age + "영화등급: " + dvd.getGrade());
        }


        dao.rentMovie(name, age, movieId,userid);

        System.out.println(
                "[대여 완료] " + name + "님이 '" + dvd.getTitle()
                        + "'(ID: " + movieId + ")를 대여했습니다."
        );
    }

    public void returnMovie(int movieId, int userId) {

        List<rentalInfoDTO>Ids = dao.findAllRents();

        rentalInfoDTO Id = null;

        for(rentalInfoDTO a : Ids) {
            if(a.getMovieId() == movieId) {
                Id = a;
                break;
            }
        }

        dao.returnMovie(movieId, userId);

    }
}