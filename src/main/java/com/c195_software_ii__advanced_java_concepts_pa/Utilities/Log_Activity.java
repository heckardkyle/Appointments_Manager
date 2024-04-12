package com.c195_software_ii__advanced_java_concepts_pa.Utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Holds LogActivity method to log User Login activity.
 * @author Kyle Heckard
 * @version 1.0
 */
public class Log_Activity {

    /**
     * Logs user activity to 'log_activity.txt'.
     * Logs Date and Time in UTC format, username entered and a description of whether it was a successful attempt
     * or if it wasn't successful and why.
     *
     * @param  username    the username to log.
     * @param  description the description to log.
     * @throws IOException
     */
    public static void LogActivity(String username, String description) throws IOException {

        // Declare location to log transaction
        String filename = "log_activity.txt";
        File   file     = new File(filename);

        // Set up PrintWriter, append to file
        FileWriter  fileWriter  = new FileWriter (file, true);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        // Convert Current date and time to UTC
        LocalDate date = LocalDate.now(Clock.systemUTC());
        LocalTime time = LocalTime.parse(LocalTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        // Log transaction
        printWriter.println(date + " " + time + " Username: " + username + "   " + description);
        System.out.println("Transaction Logged");

        // End Log
        printWriter.close();
    }
}
