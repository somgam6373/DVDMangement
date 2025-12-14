package com.example.dvdmangement.dao;

import com.example.dvdmangement.dto.ResponseDTO;
import com.example.dvdmangement.dto.UserDTO;
import com.example.dvdmangement.dto.rentalInfoDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class dvdDao {
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

            rs=stmt.executeQuery("SELECT " +
                    "    M.Movie_ID, M.제목, M.발매일, M.관객수, M.관람연령, " +
                    "    CASE " +
                    "        WHEN COUNT(R.Movie_ID) = 0 THEN 1 " +
                    "        ELSE 0 " +
                    "    END AS '판매가능여부' " +
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

    public List<rentalInfoDTO> findAllRents(int user_id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<rentalInfoDTO> rentalList = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);


            String sql = "SELECT " +
                    "    U.User_ID, " +
                    "    U.이름, " +
                    "    U.나이, " +
                    "    R.Rental_ID, " +
                    "    M.제목, " +
                    "    R.대여일, " +
                    "    M.Movie_ID " +
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
            pstmt.setInt(1, user_id);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("이름");
                int userAge = rs.getInt("나이");
                int Id = rs.getInt("Rental_ID");
                String title = rs.getString("제목");
                String rentalDate = rs.getString("대여일");
                int moiveId = rs.getInt("Movie_ID");

                rentalInfoDTO rentaldto = new rentalInfoDTO(userID, userName, userAge, Id, title, rentalDate, moiveId);
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

    public void rentMovie(int userId, int movieId) {
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

            conn.commit();

        } catch (SQLException ex) {
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

    public void returnMovie(int rental_id) {
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

    public List<UserDTO> findAllUser() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        List<UserDTO> Userlist= new ArrayList<>();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            // 쿼리 내용은 그대로 유지
            rs = stmt.executeQuery(
                    "SELECT user_id, 이름, 나이, 아이디, 비밀번호 FROM user"
            );
            while(rs.next()) {
                int userid = rs.getInt("user_id");
                String name = rs.getString("이름");
                int age = rs.getInt("나이");
                String id = rs.getString("아이디");
                String password = rs.getString("비밀번호");
                UserDTO userdto= new UserDTO(userid,name,age,id,password);
                Userlist.add(userdto);
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
        return Userlist;
    }
    // ------------------------------------------------------------------------------------------------------------------
}