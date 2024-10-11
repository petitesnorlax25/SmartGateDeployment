package com.smartgate.main;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.smartgate.main.service.SerialCommunicationService;

@SpringBootApplication
@ComponentScan(basePackages = "com.smartgate.main") // Ensure this package includes your repositories and services
public class SmartGateApplication {
    public static void main(String[] args) {
    	
    	DateTime dateTime = new DateTime();
        SpringApplication.run(SmartGateApplication.class, args);
        System.out.println("Asfdasdsad");
        System.out.println(dateTime.getCurrentTime());
		System.out.println(dateTime.getCurrentDate());
		
    }
}

