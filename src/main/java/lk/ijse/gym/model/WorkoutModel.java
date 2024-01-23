package lk.ijse.gym.model;

import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.WorkoutDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkoutModel {

    public  boolean deletePlan(String planId) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM workoutPlan WHERE planId= ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, planId);

        return pstm.executeUpdate() > 0;
    }

         public boolean savePlan(WorkoutDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO workoutPlan VALUES (?,?,?,?,?)";
        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1,dto.getPlanId());
        pstm.setString(2, dto.getWarmUp());
        pstm.setString(3, dto.getTraining());
        pstm.setString(4,dto.getCardio());
        pstm.setString(5,dto.getWarmDown());

        boolean isSaved=pstm.executeUpdate()>0;

        return  isSaved;
    }

    public boolean updatePlan(WorkoutDto dto) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="UPDATE workoutPlan SET warmUp=?,training=?,cardio=?,warmDown=? WHERE planId=?";

        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1, dto.getWarmUp());
        pstm.setString(2, dto.getTraining());
        pstm.setString(3, dto.getCardio());
        pstm.setString(4,dto.getWarmDown());
        pstm.setString(5,dto.getPlanId());

        return pstm.executeUpdate()>0;
    }

    public List<WorkoutDto> getAllPlans() throws SQLException {

        Connection connection= DbConnection.getInstance().getConnection();
        String sql="SELECT * FROM workoutPlan";

        PreparedStatement pstm=connection.prepareStatement(sql);

        ResultSet resultSet=pstm.executeQuery();

        ArrayList<WorkoutDto> dtoList=new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(
                    new WorkoutDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    )
            );
        }
        return dtoList;
    }

    public WorkoutDto searchPlan(String planId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM workoutPlan WHERE planId =? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, planId);

        ResultSet resultSet = pstm.executeQuery();

        WorkoutDto dto = null;

        if (resultSet.next()) {
            String planid = resultSet.getString(1);
            String warmUp = resultSet.getString(2);
            String training = resultSet.getString(3);
            String cardio = resultSet.getString(4);
            String warmDown = resultSet.getString(5);

            dto = new WorkoutDto(planid, warmUp, training, cardio, warmDown);
        }
        return dto;
    }

}
