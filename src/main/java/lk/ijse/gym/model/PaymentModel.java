package lk.ijse.gym.model;

import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.PaymentDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentModel {
    public PaymentDto searchPayment(String paymentId) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="SELECT * FROM payment WHERE paymentId=?";

        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1,paymentId);

        ResultSet resultSet= pstm.executeQuery();

        PaymentDto dto=null;

        if(resultSet.next()){
            String PaymentId=resultSet.getString(1);
            String memId=resultSet.getString(2);
            String memName=resultSet.getString(3);
            String plan=resultSet.getString(4);
            double amountPaid= resultSet.getDouble(5);
            String date=resultSet.getString(6);

            dto=new PaymentDto(PaymentId,memId,memName,plan,amountPaid,date);
        }
       return dto;
    }
    public boolean savePayment(PaymentDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO payment VALUES (?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getPaymentId());
        pstm.setString(2, dto.getMemberId());
        pstm.setString(3, dto.getName());
        pstm.setString(4,(dto.getPlan()));
        pstm.setDouble(5, (dto.getAmountPaid()));
        pstm.setString(6, dto.getDate());

        boolean isSaved=pstm.executeUpdate()>0;
        return  isSaved;
    }

    public List<PaymentDto> getAllPayment() throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="SELECT * FROM payment";
        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet=pstm.executeQuery();

        ArrayList<PaymentDto>dtoList=new ArrayList<>();

        while (resultSet.next()){
            dtoList.add(
                    new PaymentDto(
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

    public  boolean deletePayment(String paymentId) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="DELETE FROM payment WHERE  paymentId=? ";
        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1,paymentId);

        return pstm.executeUpdate()>0;
    }

    public  boolean updatePayment(PaymentDto dto) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="UPDATE payment set memberId=?,name=?,plan=?,amountPaid=?,date=? WHERE paymentId=?";
        PreparedStatement pstm=connection.prepareStatement(sql);

        pstm.setString(1,dto.getMemberId());
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getPlan());
        pstm.setDouble(4,dto.getAmountPaid());
        pstm.setString(5, dto.getDate());
        pstm.setString(6, dto.getPaymentId());
        return pstm.executeUpdate()>0;

    }

}

