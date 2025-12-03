package com.example.dvdmangement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor   // 기본 생성자
public class UserDTO {

    private String userId;        // user_id
    private String username;      // JSON: "username"
    private String password;      // JSON: "password"
    private Integer age;              // JSON: "age"

    private Integer rentDvdId;        // 기본 0 = 아직 대여 없음
    private String rentDvdTitle;  // null = 제목 없음
}
