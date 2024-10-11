//package com.smartgate.main.controller;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.smartgate.main.service.SerialCommunicationService;
//
//@RestController
//public class ArduinoController {
//
//    @Autowired
//    private SerialCommunicationService serialService;
//
//    @GetMapping("/control")
//    public String controlServo(@RequestParam String command) {
//        serialService.sendCommand(command);
//        try {
//            Thread.sleep(500); // Wait for Arduino to process and respond
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return serialService.readResponse();
//    }
//}
//
//
//
