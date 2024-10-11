package com.smartgate.main;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTime {
    // Method to return the current datetime as a formatted string
    public String getCurrentDateTime() {
        // Get the current datetime
        LocalDateTime now = LocalDateTime.now();

        // Define the date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd yyyy HH:mm:ss");

        // Format the LocalDateTime instance
        String formattedDateTime = now.format(formatter);

        // Return the formatted date and time
        return formattedDateTime;
    }
    public String getCurrentDate() {
    	
        // Get the current datetime
        LocalDateTime now = LocalDateTime.now();

        // Define the date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd yyyy");

        // Format the LocalDateTime instance
        String formattedDateTime = now.format(formatter);

        // Return the formatted date and time
        return formattedDateTime;
    }
    public String getCurrentTime() {
    	Date now = new Date();

        // Create a SimpleDateFormat instance with the desired format
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");

        // Format the current time to 12-hour format with AM/PM
        String formattedTime = sdf.format(now);

        // Output the formatted time
        System.out.println("Formatted Time: " + formattedTime);
		return formattedTime;
    }
    public String getCurrentDay() {
    	 Date now = new Date();

         // Create a SimpleDateFormat instance with the desired format
         SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

         // Format the current date to include the day name
         String dayName = sdf.format(now);
         return dayName;
    }
}
