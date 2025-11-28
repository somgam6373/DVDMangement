package com.example.dvdmangement.service;

import java.util.List;
import com.example.dvdmangement.dto.ResponseDTO;
import com.example.dvdmangement.dao.dvdDao;
import org.springframework.stereotype.Service;

@Service
public class dvdService {
    private dvdDao dao = new dvdDao();

    public List<ResponseDTO> getAllDvds() {
        return dao.findAllDvd();
    }
}