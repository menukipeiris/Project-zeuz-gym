package lk.ijse.gym.model;

import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.MemberDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberModel {

    public MemberDto searchMember(String memberId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM member WHERE memberId =? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, memberId);

        ResultSet resultSet = pstm.executeQuery();

        MemberDto dto = null;

        if (resultSet.next()) {
            String mem_Id = resultSet.getString(1);
            String mem_name = resultSet.getString(2);
            String mem_gender = resultSet.getString(3);
            int mem_con = Integer.parseInt(resultSet.getString(4));
            String mem_add=resultSet.getString(5);
            String mem_dateJoin=resultSet.getString(6);
            String mem_plan=resultSet.getString(7);
            double plan_price = resultSet.getDouble(8);

            dto=new MemberDto(mem_Id,mem_name,mem_gender,mem_con,mem_add,mem_dateJoin,mem_plan,plan_price);
        }
        return dto;
    }


    public boolean saveMember(MemberDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO member VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1,dto.getMemberId());
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getGender());
        pstm.setInt(4,dto.getCont());
        pstm.setString(5,dto.getAddress());
        pstm.setString(6, dto.getDateJoin());
        pstm.setString(7, dto.getPlan());
        pstm.setInt(8, (int) dto.getPlanPrice());

        boolean isSaved=pstm.executeUpdate()>0;

        return  isSaved;
    }

    public List<MemberDto> getAllMember() throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="SELECT * FROM member";
        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet=pstm.executeQuery();

        ArrayList<MemberDto>dtoList=new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(
                    new MemberDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getInt(4),
                            resultSet.getString(5),
                            resultSet.getString(6),
                            resultSet.getString(7),
                            resultSet.getDouble(8)


                    )
            );
        }
            return dtoList;
        }
    public boolean deleteMember(String memberId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM member WHERE memberId= ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, memberId);

        return pstm.executeUpdate() > 0;
    }


    public boolean updateMember(MemberDto dto) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="UPDATE member SET name = ?, gender = ?, contact = ?,address = ?,dateJoin=?,plan=?,planPrice=? WHERE memberId = ?";
        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getGender());
        pstm.setString(3, String.valueOf(dto.getCont()));
        pstm.setString(4,dto.getAddress());
        pstm.setString(5,dto.getDateJoin());
        pstm.setString(6, dto.getPlan());
        pstm.setDouble(7,dto.getPlanPrice());
        pstm.setString(8,dto.getMemberId());
        return pstm.executeUpdate()>0;
    }
}



