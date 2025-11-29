package com.example.dvdmangement.dao;

import com.example.dvdmangement.dto.ResponseDTO;

import java.sql.*;
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
                for(int i=0; i<movieList.size(); i++) {
                    System.out.println(movieList.get(i).getTitle());}
            }catch (SQLException ex) {
                ex.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
            return movieList;
        }
    }
