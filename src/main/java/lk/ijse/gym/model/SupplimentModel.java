package lk.ijse.gym.model;

import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.SupplimentDto;
import lk.ijse.gym.dto.tm.CartTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplimentModel {

    public static List<SupplimentDto> loadAllItems() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM suppliment";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<SupplimentDto> itemList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            itemList.add(new SupplimentDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            ));
        }

        return itemList;
    }

    public static boolean saveItem(SupplimentDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO suppliment VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getCode());
        pstm.setString(2, dto.getDescription());
        pstm.setDouble(3, dto.getUnitPrice());
        pstm.setInt(4, dto.getQtyOnHand());

        return pstm.executeUpdate() > 0;
    }

    public static boolean updateItem(SupplimentDto supplimentDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE suppliment SET description = ?, unit_price = ?, qty_on_hand = ? WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, supplimentDto.getDescription());
        pstm.setDouble(2, supplimentDto.getUnitPrice());
        pstm.setInt(3, supplimentDto.getQtyOnHand());
        pstm.setString(4, supplimentDto.getCode());

        return pstm.executeUpdate() > 0;
    }
    public static boolean updateItem(List<CartTm> cartTmList) throws SQLException {
        for(CartTm tm : cartTmList) {
            System.out.println("Item: " + tm);
            if(!updateQty(tm.getCode(), tm.getQty())) {
                return false;
            }
        }
        return true;
    }

    public SupplimentDto searchItem(String code) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM suppliment WHERE  code = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, code);

        ResultSet resultSet = pstm.executeQuery();

        SupplimentDto dto = null;

        if(resultSet.next()) {
            dto = new SupplimentDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        }
        return dto;
    }
    public static boolean updateQty(String code, int qty) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE suppliment SET qty_on_hand = qty_on_hand - ? WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setInt(1, qty);
        pstm.setString(2, code);

        return pstm.executeUpdate() > 0; //false
    }
}
