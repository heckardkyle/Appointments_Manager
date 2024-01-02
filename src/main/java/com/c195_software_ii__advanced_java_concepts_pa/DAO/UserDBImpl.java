package com.c195_software_ii__advanced_java_concepts_pa.DAO;

import com.c195_software_ii__advanced_java_concepts_pa.Models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Database Access Object for User Queries.
 * A userName and password are used to check if a User exists in the database and, if so, a User object is returned
 * to allow the user to log in to application.
 *
 * @author Kyle Heckard
 * @version 1.0
 * @see User
 */
public class UserDBImpl {

    static String            sql = "SELECT * FROM users WHERE User_Name = ?"; // Query
    static PreparedStatement preparedStatement;
    static ResultSet         result;
    static User              userResult;

    /**
     * Checks userName and password against database to log in to application.
     * First checks if userName exists in database. If it exists, the password is verified. If the password
     * matches, a User Object is returned. If the userName or password is incorrect, return null.
     *
     * @param userName userName to search for
     * @param password password to verify
     * @return <code>userResult</code> if params verified, otherwise null.
     * @throws SQLException if Query fails.
     */
    public static User GetUser(String userName, String password) throws SQLException {
        try {
            preparedStatement = JDBC.connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            result = preparedStatement.executeQuery();

            while (result.next()) { // userName exists in database
                // Check if password matches and return User
                if (Objects.equals(password, result.getString("Password"))) {
                    int    userID    = result.getInt   ("User_ID");
                    String userName1 = result.getString("User_Name");
                    userResult = new User(userID, userName1);
                    return userResult;
                }
            }
        }
        catch (SQLException e) { e.printStackTrace(); }
        return null; // userName or password does not match.
    }
}
