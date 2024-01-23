package lk.ijse.gym.model;

import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    public static String getEmail(String email) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT email FROM user WHERE email=?");
        statement.setObject(1,email );
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public static boolean saveUser(UserDto userDto) throws SQLException {
            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user VALUES (?,?,?)");
            statement.setObject(1, userDto.getEmail());
            statement.setObject(2, userDto.getName());
            statement.setObject(3, userDto.getPassword());


            int i = statement.executeUpdate();
            return 0 < i;

        }
   public static boolean isExistUser(String userName, String pw) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT password ,name ,email FROM user WHERE name=? AND password=?");
        statement.setObject(1, userName);
        statement.setObject(2, pw);
        ResultSet resultSet = statement.executeQuery();
        String schemaUserName = null;
        String schemaPassword = null;
        if (resultSet.next()) {
            schemaPassword = resultSet.getString(1);
            schemaUserName = resultSet.getString(2);
            DbConnection.email = resultSet.getString(3);
        }
        return userName.equals(schemaUserName) && pw.equals(schemaPassword);
    }
}
