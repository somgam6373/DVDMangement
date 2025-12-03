package com.example.dvdmangement.dao;

import com.example.dvdmangement.dto.UserDTO;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserDao {

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost/mydb?serverTimezone=Asia/Seoul",
                "root",
                "0211"
        );
    }

    // username으로 유저 찾기
    public UserDTO findByUsername(String userId) {
        // 로그인 아이디(username) -> user_id 컬럼으로 조회
        String sql = "SELECT * FROM reuser WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserDTO user = new UserDTO();
                    user.setUserId(String.valueOf(rs.getInt("User_ID")));          // PK
                    user.setUserId(rs.getString("id"));                // 로그인 아이디

                    user.setPassword(rs.getString("password"));
                    user.setAge(rs.getInt("age"));
                    user.setRentDvdId((Integer) rs.getObject("rentDvdId"));
                    user.setRentDvdTitle(rs.getString("rentDvdTitle"));
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 회원 저장 (회원가입)
    public void save(UserDTO user) {
        String sql = "INSERT INTO reuser(id, username, password, age, rentDvdId, rentDvdTitle) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 1) 로그인 아이디 → user_id
            pstmt.setString(1, user.getUserId());

            // 2) 실제 이름 → username (프론트에서 name을 받으면 DTO에 name 필드 하나 더 추가)
            // 일단 이름을 안 쓰면 공백으로 넣어두자
            pstmt.setString(2, user.getUsername()); // 또는 user.getName()

            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getAge());

            Integer rentDvdId = user.getRentDvdId();
            if (rentDvdId == null) {
                pstmt.setNull(5, Types.INTEGER);
            } else {
                pstmt.setInt(5, rentDvdId);
            }

            String rentTitle = user.getRentDvdTitle();
            if (rentTitle == null || rentTitle.isEmpty()) {
                pstmt.setNull(6, Types.VARCHAR);
            } else {
                pstmt.setString(6, rentTitle);
            }

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
