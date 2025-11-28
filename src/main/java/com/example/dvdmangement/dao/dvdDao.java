package com.example.dvdmangement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class dvdDao {
    public List<ResponseDTO> findAll() {
        List<ResponseDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM Movie";

        try (
                Connection conn = Main.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
        ) {
            while (rs.next()) {
                ResponseDTO dto = new ResponseDTO(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("audience"),
                        rs.getString("date"),
                        rs.getInt("grade"),
                        rs.getBoolean("available")
                );
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
