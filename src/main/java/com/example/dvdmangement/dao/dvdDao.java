package com.example.dvdmangement.dao;

import com.example.dvdmangement.dto.ResponseDTO;
import com.example.dvdmangement.dto.rentalInfoDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

    public class dvdDao {
        public List<ResponseDTO> findAllDvd() {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            List<ResponseDTO> movieList = new ArrayList<>();

            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection("jdbc:mysql://localhost/mydb?serverTimezone=Asia/Seoul", "root","0211");
                stmt = conn.createStatement();
                rs=stmt.executeQuery("select * from movie");
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
            }
            return movieList;
        }
        public void rentMovie(String name, int age, int movieId) {
            LocalDateTime rentalDate = LocalDateTime.now();

            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost/mydb?serverTimezone=Asia/Seoul",
                        "root",
                        "0211"
                );
                conn.setAutoCommit(false);

                // 영화 정보 조회

                // user INSERT
                String insertUserSql = "INSERT INTO user (이름, 나이) VALUES (?, ?)";
                pstmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, name);
                pstmt.setInt(2, age);
                pstmt.executeUpdate();

                rs = pstmt.getGeneratedKeys();
                int userId = (rs.next()) ? rs.getInt(1) : -1;
                rs.close();
                pstmt.close();


                // movie 상태 업데이트
                String updateMovieSql = "UPDATE movie SET 판매가능여부 = 0 WHERE Movie_ID = ?";
                pstmt = conn.prepareStatement(updateMovieSql);
                pstmt.setInt(1, movieId);
                pstmt.executeUpdate();
                pstmt.close();

                // rental INSERT
                String insertRentalSql =
                        "INSERT INTO rental (Movie_ID, User_ID, 대여일) VALUES (?,?,?)";
                pstmt = conn.prepareStatement(insertRentalSql);
                pstmt.setInt(1, movieId);
                pstmt.setInt(2, userId);
                pstmt.setObject(3, rentalDate);
                pstmt.executeUpdate();

                conn.commit();

            } catch (Exception ex) {
                try {
                    if (conn != null) conn.rollback();
                } catch (SQLException ignored) {
                }

                throw new RuntimeException("대여 처리 중 오류 발생: " + ex.getMessage(), ex);

            } finally {
                try {
                    if (rs != null) rs.close();
                } catch (Exception ignored) {
                }
                try {
                    if (pstmt != null) pstmt.close();
                } catch (Exception ignored) {
                }
                try {
                    if (conn != null) conn.close();
                } catch (Exception ignored) {
                }
            }

        }
        public List<rentalInfoDTO> findAllRents(){
            return null;
        }

        public void returnMovie(int movieid, int userid){
            Connection conn = null;
        }
    }
