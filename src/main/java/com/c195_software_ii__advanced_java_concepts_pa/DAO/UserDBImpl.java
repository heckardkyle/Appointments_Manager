package com.c195_software_ii__advanced_java_concepts_pa.DAO;

import com.c195_software_ii__advanced_java_concepts_pa.Models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserDBImpl {

    static String sql = "SELECT * FROM users WHERE User_Name = ?";
    static PreparedStatement preparedStatement;
    static ResultSet result;
    static User userResult;

    public static User GetUser(String userName, String password) throws SQLException {

        preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setString(1, userName);
        result = preparedStatement.executeQuery();

        while (result.next()){
            if (Objects.equals(password, result.getString("Password"))) {
                int userID = result.getInt("User_ID");
                String userName1 = result.getString("User_Name");
                userResult = new User(userID, userName1);
                return userResult;
            }
        }
        return null;
    }
}
