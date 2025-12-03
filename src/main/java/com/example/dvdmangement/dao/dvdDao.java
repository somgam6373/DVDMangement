package com.example.dvdmangement.dao;

import com.example.dvdmangement.dto.ResponseDTO;
import com.example.dvdmangement.dto.rentalInfoDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class dvdDao {
    // JDBC 연결 정보 (클래스 레벨 상수로 정의하여 재사용성 및 관리 용이성 향상)
    private static final String DB_URL = "jdbc:mysql://localhost/mydb?serverTimezone=Asia/Seoul";
    private static final String USER = "root";
    private static final String PASS = "0211";

    public List<ResponseDTO> findAllDvd() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        List<ResponseDTO> movieList = new ArrayList<>();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            // 쿼리 내용은 그대로 유지
            rs=stmt.executeQuery("SELECT " +
                    "    M.Movie_ID, M.제목, M.발매일, M.관객수, M.관람연령, " +
                    "    CASE " +
                    "        WHEN COUNT(R.Movie_ID) = 0 THEN 1 " +
                    "        ELSE 0 " +
                    "    END AS 판매가능여부 " +
                    "FROM " +
                    "    Movie M " +
                    "LEFT JOIN " +
                    "    Rental R ON M.Movie_ID = R.Movie_ID AND R.반납일 IS NULL " +
                    "GROUP BY " +
                    "    M.Movie_ID, M.제목, M.발매일, M.관객수, M.관람연령 " +
                    "ORDER BY " +
                    "    M.발매일 DESC");
            while(rs.next()) {
                int id = rs.getInt("Movie_ID");
                String title = rs.getString("제목");
                String date = rs.getString("발매일");
                String audience = rs.getString("관객수");
                int grade = rs.getInt("관람연령");
                boolean available = rs.getBoolean("판매가능여부");
                ResponseDTO movieDto = new ResponseDTO(id, title, audience, date, grade, available);
                movieList.add(movieDto);
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) { }
            try { if (conn != null) conn.close(); } catch (SQLException ignored) { }
        }
        return movieList;
    }

    // ------------------------------------------------------------------------------------------------------------------

    public List<rentalInfoDTO> findAllRents(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<rentalInfoDTO> rentalList = new ArrayList<>(); // DTO 타입 일치

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // conn.setAutoCommit(false); // 조회(SELECT)에는 필요 없으므로 제거

            String sql = "SELECT " +
                    "    M.제목, " +
                    "    R.대여일, " +
                    "    R.Rental_ID " +
                    "FROM " +
                    "    User U " +
                    "JOIN " +
                    "    Rental R ON U.User_ID = R.User_ID " +
                    "JOIN " +
                    "    Movie M ON R.Movie_ID = M.Movie_ID " +
                    "WHERE " +
                    "    U.User_ID = ? " +
                    "    AND R.반납일 IS NULL";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("제목");
                String rentalDate = rs.getString("대여일");
                int rentalId = rs.getInt("Rental_ID");

                // 💡 버그 수정: rentalInfoDTO 생성자에 맞게 title, rentalDate, rentalId 전달
                rentalInfoDTO rentaldto = new rentalInfoDTO(title, rentalDate, rentalId);
                rentalList.add(rentaldto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) { }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException ignored) { }
            try { if (conn != null) conn.close(); } catch (SQLException ignored) { }
        }
        return rentalList;
    }

    // ------------------------------------------------------------------------------------------------------------------

    public void rentMovie(int movieId, int userId) {
        LocalDateTime rentalDate = LocalDateTime.now();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);

            String insertRentalSql= "INSERT INTO rental (Movie_ID, User_ID, 대여일, 반납일) VALUES (?,?,?,null)";
            pstmt = conn.prepareStatement(insertRentalSql);
            pstmt.setInt(1, movieId);
            pstmt.setInt(2, userId);
            pstmt.setObject(3, rentalDate);
            pstmt.executeUpdate();

            conn.commit(); // 트랜잭션 완료

        } catch (SQLException ex) { // 💡 SQLException을 명시적으로 잡고 롤백
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) { }
            throw new RuntimeException("대여 처리 중 DB 오류 발생: " + ex.getMessage(), ex);
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) { }
            throw new RuntimeException("대여 처리 중 오류 발생: " + e.getMessage(), e);
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (Exception ignored) { }
            try { if (conn != null) conn.close(); } catch (Exception ignored) { }
        }
    }

    // ------------------------------------------------------------------------------------------------------------------

    public void returnMovie(int rental_id){
        LocalDateTime returnDate = LocalDateTime.now(); // 변수 이름 수정
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);

            String returnRentalSql= "UPDATE rental SET 반납일=? WHERE Rental_ID=?";
            pstmt = conn.prepareStatement(returnRentalSql);

            pstmt.setObject(1, returnDate);
            // 💡 버그 수정: rental_id 바인딩 추가
            pstmt.setInt(2, rental_id);

            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) { }
            throw new RuntimeException("반납 처리 중 DB 오류 발생: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ignored) {
            }
            throw new RuntimeException("반납 처리 중 오류 발생: " + ex.getMessage(), ex);
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (Exception ignored) { }
            try { if (conn != null) conn.close(); } catch (Exception ignored) { }
        }
    }

    // ------------------------------------------------------------------------------------------------------------------

    public boolean signUp(String name, int age, String id, String pw) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);

            String insertUserSql = "INSERT INTO User (이름, 나이, 아이디, 비밀번호) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertUserSql);

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, id);
            pstmt.setString(4, pw);

            int affectedRows = pstmt.executeUpdate();
            boolean success1 = true;

        } catch (SQLIntegrityConstraintViolationException ex) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) { }
            return false;
        } catch (Exception ex) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ignored) {
            }
            return false;
        } finally {
            // 3. 자원 해제
            try { if (pstmt != null) pstmt.close(); } catch (Exception ignored) { }
            try { if (conn != null) conn.close(); } catch (Exception ignored) { }
        }
        return success;
    }
    public int loginCheck(String inputId, String inputPw) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int success = 0; // 0: 실패, 1: 성공

        try {
            // ... DB 연결 및 초기화 ...

            String sql = "SELECT COUNT(User_ID) AS login_success FROM User WHERE 아이디 = ? AND 비밀번호 = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, inputId); // 1. 입력받은 아이디 바인딩
            pstmt.setString(2, inputPw); // 2. 입력받은 비밀번호 바인딩

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // 결과는 0 또는 1
                success = rs.getInt("login_success");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            // 오류 발생 시 0 반환
            return 0;
        } finally {
            // ... 자원 해제 ...
        }

        return success; // 1 (성공) 또는 0 (실패) 반환
    }

}