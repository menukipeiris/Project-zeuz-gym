package lk.ijse.gym.model;

import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.EmployeeDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    public List<EmployeeDto> getAllTrainers() throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();

        String sql="SELECT * FROM employee";

        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet=pstm.executeQuery();

        ArrayList<EmployeeDto>dtoList=new ArrayList<>();

        while(resultSet.next()){
            dtoList.add(
                    new EmployeeDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getString(6)
            )
            );
        }
        return  dtoList;
    }

    public boolean deleteTrainer(String trainerId) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="DELETE FROM employee WHERE trainerId=?";

        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1,trainerId);

        return pstm.executeUpdate()>0;
    }

    public boolean saveTrainer(EmployeeDto dto) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="INSERT INTO employee VALUES(?,?,?,?,?,?)";

        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1,dto.getTrainerId());
        pstm.setString(2,dto.getName());
        pstm.setString(3,dto.getContactNu());
        pstm.setString(4,dto.getAddress());
        pstm.setString(5, dto.getRole());
        pstm.setString(6,dto.getAvailability());

        boolean isSaved=pstm.executeUpdate()>0;

        return isSaved;
    }

    public boolean updateTrainer(EmployeeDto dto) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql="UPDATE employee set name=?,contactNu=?,address=?,role=?,availability=? WHERE trainerId=?";

        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1,dto.getName());
        pstm.setString(2,dto.getContactNu());
        pstm.setString(3,dto.getAddress());
        pstm.setString(4,dto.getRole());
        pstm.setString(5,dto.getAvailability());
        pstm.setString(6, dto.getTrainerId());

        return  pstm.executeUpdate()>0;
    }

    public EmployeeDto searchTrainer(String trainerId) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="SELECT * FROM employee WHERE trainerId=?";

       PreparedStatement pstm=connection.prepareStatement(sql);

       pstm.setString(1,trainerId);
        ResultSet resultSet = pstm.executeQuery();

       EmployeeDto dto=null;

       if(resultSet.next()){
           String trainerid=resultSet.getString(1);
           String name=resultSet.getString(2);
           String contact=resultSet.getString(3);
           String address=resultSet.getString(4);
           String role=resultSet.getString(5);
           String availability=resultSet.getString(6);

           dto=new EmployeeDto(trainerid,name,contact,address,role,availability);
       }
       return dto;
    }
}
