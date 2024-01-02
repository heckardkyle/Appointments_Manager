package com.c195_software_ii__advanced_java_concepts_pa.DAO;

import com.c195_software_ii__advanced_java_concepts_pa.Models.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database Access Object for Country Queries.
 * Countries are fetched from database and turned into Country Objects for use in choice boxes when adding or
 * updating a Customer.
 *
 * @author Kyle Heckard
 * @version 1.0
 * @see Country
 */
public class CountryDBImpl {

    static PreparedStatement preparedStatement;
    static ResultSet         result;
    static Country           country;

    /**
     * Fetches all Countries from the Database.
     * A Country Object is created from each result in the query. Each Country is added to ObservableList countries.
     * The ObservableList is returned.
     *
     * @return <code>countries</code>
     * @throws SQLException if Query fails
     */
    public static ObservableList<Country> getCountries() throws SQLException {
        try {
            String sqlSelect = "SELECT * FROM countries"; // Query
            ObservableList<Country> countries = FXCollections.observableArrayList(); // Table for storing Country Objects
            countries.clear(); // Clear table, prevents potential duplicates
            preparedStatement = JDBC.connection.prepareStatement(sqlSelect);
            result = preparedStatement.executeQuery();

            while (result.next()) { // For each result in query
                // Store each value from result
                int    countryID   = result.getInt   ("Country_ID");
                String countryName = result.getString("Country");

                // Create Country Object using stored values and add to ObservableList
                country = new Country(countryID, countryName);
                countries.add(country);
            }
            return countries;
        }
        // return null upon exception
        catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

}
