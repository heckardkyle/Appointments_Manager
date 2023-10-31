package com.c195_software_ii__advanced_java_concepts_pa.Utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Log_Activity {

    public static void LogActivity(String username, String description) throws IOException {

        String filename = "log_activity.txt";
        File file = new File(filename);
        PrintWriter printWriter;
        FileWriter fileWriter;

        fileWriter = new FileWriter(file, true);
        printWriter = new PrintWriter(fileWriter);

        LocalDate date = LocalDate.now(Clock.systemUTC());
        LocalTime time = LocalTime.parse(LocalTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        printWriter.println(date + " " + time + " Username: " + username + "   " + description);

        System.out.println("Transaction Logged");

        printWriter.close();

    }
}
