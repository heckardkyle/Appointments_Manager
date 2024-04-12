package com.c195_software_ii__advanced_java_concepts_pa.DAO;

import com.c195_software_ii__advanced_java_concepts_pa.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Database Access Object for User Queries.
 * A userName and password are used to check if a User exists in the database and, if so, a User object is returned
 * to allow the user to log in to application. Also used for creating appointments and reports.
 *
 * @author Kyle Heckard
 * @version 1.0
 * @see User
 */
public class UserDBImpl {

    static PreparedStatement preparedStatement;
    static ResultSet         result;
    static User              userResult;

    /**
     * Fetches all UserIDs from the Database.
     * A User Object is created from each result in query. Each User Object is added to ObservableList allUserIDs.
     * The ObservableList is returned.
     *
     * @return <code>allUserIDs</code>
     * @throws SQLException
     */
    public static ObservableList<User> getAllUserIDs() throws SQLException {
        try {
            // Setup Select query, ObservableList, then execute query
            String sqlSelect = "SELECT * FROM users"; // Query
            ObservableList<User> allUserIDs = FXCollections.observableArrayList(); // List for storing User Objects
            allUserIDs.clear(); // clear list, prevents potential duplication
            preparedStatement = JDBC.connection.prepareStatement(sqlSelect);
            result = preparedStatement.executeQuery();

            // Retrieve users, create User Objects, and store in ObservableList
            while (result.next()) { // For each result in query

                // retrieve userID, create User Object, and store in ObservableList
                int userID = result.getInt("User_ID");
                userResult = new User(userID);
                allUserIDs.add(userResult);
            }
            // return ObservableList
            return allUserIDs;
        }
        // return null upon exception or if no results
        catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Checks userName and password against database to log in to application.
     * First checks if userName exists in database. If it exists, the password is verified. If the password
     * matches, a User Object is returned. If the userName or password is incorrect, return null.
     * User if found using userName.
     *
     * @param userName userName to search for
     * @param password password to verify
     * @return <code>userResult</code> if params verified, otherwise null.
     * @throws SQLException if Query fails.
     */
    public static User getUser(String userName, String password) throws SQLException {
        try {
            // Setup Select query then execute
            String sqlSelect = "SELECT * FROM users WHERE User_Name = ?"; // Query
            preparedStatement = JDBC.connection.prepareStatement(sqlSelect);
            preparedStatement.setString(1, userName); // argument for query
            result = preparedStatement.executeQuery();

            // Retrieve result, and verify password
            while (result.next()) { // userName exists in database

                // If password matches, return User Object
                if (Objects.equals(password, result.getString("Password"))) {
                    int    userID    = result.getInt   ("User_ID");
                    String userName1 = result.getString("User_Name");
                    userResult = new User(userID, userName1);
                    return userResult;
                }
            }
        }
        // return null upon exception or if User not found
        catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Searches for UserID in database and return User Object.
     * Purpose is to check if User exists in database when creating appointment.
     *
     * @param userID The userID to search for
     * @return <code>userResult</code>
     * @throws SQLException
     */
    public static User getUser(String userID) throws SQLException {
        try {
            // Setup Select query, then execute query
            String sqlSelect = "SELECT * FROM users WHERE User_ID = ?"; // Query
            preparedStatement = JDBC.connection.prepareStatement(sqlSelect);
            preparedStatement.setString(1, userID); // arguments for query
            result = preparedStatement.executeQuery();

            // If userID exists, return User Object
            while (result.next()) { // userName exists in database

                // Create User Object and return User
                int    userID1    = result.getInt   ("User_ID");
                String userName1 = result.getString("User_Name");
                userResult = new User(userID1, userName1);
                return userResult;
            }
        }
        // return null upon exception or if no result
        catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}
