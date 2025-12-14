package com.example.dvdmangement.controller;

import com.example.dvdmangement.QrGenerator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/qr")
public class QrController {

    private final QrGenerator qrGenerator;

    public QrController(QrGenerator qrGenerator) {
        this.qrGenerator = qrGenerator;
    }
    @GetMapping("/dvd")
    public ResponseEntity<byte[]> getDvdQr(@RequestParam int dvdId, @RequestParam String userId) throws Exception {

        String qrText =
                "{"
                        + "\"userId\":\"" + userId + "\","
                        + "\"dvdId\":" + dvdId + ","
                        + "}";

        byte[] image = qrGenerator.generateQr(qrText);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }
}

