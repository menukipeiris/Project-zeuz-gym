package lk.ijse.gym.model;


import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.ServiceDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServiceModel {

    public List<ServiceDto> getAllClasses() throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();

        String sql="SELECT * FROM service";

        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet= pstm.executeQuery();

        ArrayList<ServiceDto>dtoList=new ArrayList<>();

        while (resultSet.next()){
            dtoList.add(
                    new ServiceDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getString(6)
                    )
            );
        }
        return dtoList;
    }

    public boolean deleteClass(String serviceId) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="DELETE FROM service WHERE serviceId=?";
        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1,serviceId);

        return pstm.executeUpdate()>0;
    }

    public boolean saveClass(ServiceDto dto) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="INSERT INTO service VALUES(?,?,?,?,?,?)";

        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1,dto.getServiceId());
        pstm.setString(2, dto.getTrainerId());
        pstm.setString(3, dto.getTrainerName());
        pstm.setString(4, dto.getClassType());
        pstm.setString(5, dto.getTime());
        pstm.setString(6, dto.getDate());

        boolean isSaved=pstm.executeUpdate()>0;
        return isSaved;
    }

    public boolean updateClass(ServiceDto dto) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="UPDATE service set trainerId=?,trainerName=?,classType=?,time=?,date=? WHERE serviceId=?";
        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1, dto.getTrainerId());
        pstm.setString(2, dto.getTrainerName());
        pstm.setString(3, dto.getClassType());
        pstm.setString(4, dto.getTime());
        pstm.setString(5, dto.getDate());
        pstm.setString(6, dto.getServiceId());

        return pstm.executeUpdate()>0;
    }

    public ServiceDto searchClass(String serviceId) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="SELECT * FROM service WHERE serviceId=?";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1,serviceId);

        ResultSet resultSet= pstm.executeQuery();

        ServiceDto dto=null;
        if(resultSet.next()){
            String service_Id=resultSet.getString(1);
            String trainerId=resultSet.getString(2);
            String trainerName=resultSet.getString(3);
            String classType=resultSet.getString(4);
            String time=resultSet.getString(5);
            String date=resultSet.getString(6);

            dto=new ServiceDto(service_Id,trainerId,trainerName,classType,time,date);
        }
        return dto;
    }
}
