package lk.ijse.gym.model;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.EquipmentDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipmentModel {

    public List<EquipmentDto> getAllEquipment() throws SQLException {

        Connection connection=DbConnection.getInstance().getConnection();
        String sql="SELECT * FROM equipment";

        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet=pstm.executeQuery();

        ArrayList<EquipmentDto> dtoList=new ArrayList<>();
        while (resultSet.next()) {
            dtoList.add(
                    new EquipmentDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getDouble(5),
                            resultSet.getString(6)
                    )
            );
        }
           return dtoList;
    }

    public boolean saveEquipment(EquipmentDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO equipment VALUES (?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dto.getEquipmentId());
        pstm.setString(2, dto.getEquName());
        pstm.setString(3, dto.getDeliveryDate());
        pstm.setString(4,(dto.getDescription()));
        pstm.setDouble(5, (dto.getCost()));
        pstm.setString(6, dto.getMuscleUsed());

        boolean isSaved=pstm.executeUpdate()>0;
        return  isSaved;
    }

    public boolean updateEquipment(EquipmentDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql="UPDATE equipment set equName=?,deliveryDate=?,description=?,cost=?,muscleUsed=? WHERE equipmentId=?";
        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1, dto.getEquName());
        pstm.setString(2, dto.getDeliveryDate());
        pstm.setString(3, dto.getDescription());
        pstm.setDouble(4,dto.getCost());
        pstm.setString(5, dto.getMuscleUsed());
        pstm.setString(6, dto.getEquipmentId());
        return pstm.executeUpdate()>0;
    }

    public boolean deleteEquipment(String equId) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql= "DELETE  FROM equipment WHERE equipmentId=?";

        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1,equId);

        return pstm.executeUpdate()>0;


    }

    public EquipmentDto searchEquipment(String equipmentId) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="SELECT * FROM equipment WHERE equipmentId=?";

        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1,equipmentId);

        ResultSet resultSet= pstm.executeQuery();

        EquipmentDto dto=null;

        if(resultSet.next()){
            String equipmetId=resultSet.getString(1);
            String equName=resultSet.getString(2);
            String deliveryDate=resultSet.getString(3);
            String description=resultSet.getString(4);
            double cost= resultSet.getDouble(5);
            String muscleUsed=resultSet.getString(6);

            dto=new EquipmentDto(equipmetId,equName,deliveryDate,description,cost,muscleUsed);
        }
        return dto;
    }

}



