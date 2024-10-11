package com.smartgate.main.controller;


import java.io.IOException;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.sql.Blob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartgate.main.DateTime;
import com.smartgate.main.entity.Logs;
import com.smartgate.main.entity.Notifications;
import com.smartgate.main.entity.SecurityAlerts;
import com.smartgate.main.entity.Students;
import com.smartgate.main.entity.StudentsData;
import com.smartgate.main.entity.Visitors;
import com.smartgate.main.repository.NotificationsRepository;
import com.smartgate.main.repository.SecurityAlertsRepository;
import com.smartgate.main.service.ApiAuthenticationService;
import com.smartgate.main.service.LogsService;
import com.smartgate.main.service.NotificationsService;
import com.smartgate.main.service.SecurityAlertsService;
import com.smartgate.main.service.SerialCommunicationService;
import com.smartgate.main.service.StudentsService;
import com.smartgate.main.service.VisitorsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.smartgate.main.SmsApi;
@Controller

public class StudentsController {
	@Autowired
	private StudentsService studentsService;
	@Autowired
	private LogsService logsService;
	@Autowired
	private SecurityAlertsService securityAlertsService;
	@Autowired
	private SecurityAlertsRepository securityAlertsRepository;
	@Autowired
	private VisitorsService visitorsService;
	@Autowired
	private NotificationsService notificationsService;
	@Autowired
	private NotificationsRepository notificationsRepository;
	@Autowired
    private SerialCommunicationService serialService;
	@Value("${api.login.url}")
    private String apiLoginUrl;
	private SmsApi smsApi;
	@Autowired
	public void SmsController(SmsApi smsApi) {
	        this.smsApi = smsApi;
	}

	 private final ApiAuthenticationService apiAuthenticationService;
	    private final SimpMessagingTemplate messagingTemplate;

	    public StudentsController(ApiAuthenticationService apiAuthenticationService, SimpMessagingTemplate messagingTemplate) {
	        this.apiAuthenticationService = apiAuthenticationService;
	        this.messagingTemplate = messagingTemplate;
	    }
//    @GetMapping("/start")
//    public String startPythonServer() {
//        try {
//            ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/python/face_recognition_server.py");
//            processBuilder.redirectErrorStream(true);
//            Process process = processBuilder.start();
//            return "Python server started";
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Error starting Python server";
//        }
//    }
//    @PostMapping("/recognize")
//    public ResponseEntity<String> recognizeFaces(@RequestBody String imageData) {
//        try {
//            ProcessBuilder processBuilder = new ProcessBuilder(
//                "python",
//                "src/main/resources/python/face_recognition_server.py",
//                imageData
//            );
//            processBuilder.redirectErrorStream(true);
//            Process process = processBuilder.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String result = reader.lines().collect(Collectors.joining("\n"));
//            return ResponseEntity.ok(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image");
//        }
//    }
    /*@PostMapping("/enterSmartGate")
    public String validateStudentMethod(
            @ModelAttribute Students students, 
            @ModelAttribute Visitors visitors, 
            @RequestParam(name="toggleState2") String toggleState, 
            HttpSession session, 
            RedirectAttributes redirectAttributes, 
            Model model) {

        DateTime dateTime = new DateTime();
        Logs logs = new Logs();
        LocalTime now = LocalTime.now();
        System.out.println(toggleState + toggleState);
        SecurityAlerts securityAlerts = new SecurityAlerts();
        Notifications notifications = new Notifications();
        notifications = notificationsRepository.getByAlertType("securityalerts");
        // Check if SmartGate is off
        if (toggleState.equalsIgnoreCase("OFF")) {
            return "redirect:/smartgate?errorMessage=THE SMARTGATE IS OFF, PLEASE TURN IT ON!";
        }

        // Fetch the student by RFID
        Students getStudent = studentsService.getStudentsByRfid(students.getRfid());

        if (getStudent == null) {
            System.out.println("Student not found for RFID: " + students.getRfid());
            securityAlerts.setAnomaly("Unknown RFID: " + students.getRfid());
            securityAlerts.setTime(dateTime.getCurrentTime());
            securityAlerts.setDate(dateTime.getCurrentDate());
            securityAlertsRepository.save(securityAlerts);
            
            Long hasNotif = 1L;
            Long counterNotif = notifications.getHasNotif()+hasNotif;
            notifications.setHasNotif(counterNotif);
            notificationsService.updateNotifications(notifications.getId(), notifications);
            securityAlertsService.createSecurityAlerts(getStudent, securityAlerts);
            
            serialService.sendCommand("close");
            return "redirect:/smartgate?errorMessage=RFID is unknown, cant access smartgate";
        }

        // Get session array list (initialize if not present)
        @SuppressWarnings("unchecked")
        List<StudentEntry> studentEntries = (List<StudentEntry>) session.getAttribute("studentEntries");
        if (studentEntries == null) {
            studentEntries = new ArrayList<>();
            session.setAttribute("studentEntries", studentEntries);
        }

        // Get the current minute and second
        int currentMinute = now.getMinute();
        int currentSecond = now.getSecond();

        // Iterate through the session entries to check for anomalies
        Iterator<StudentEntry> iterator = studentEntries.iterator();
        boolean foundThirdTap = false;
        while (iterator.hasNext()) {
            StudentEntry entry = iterator.next();
            // Calculate the time difference in seconds
            int timeDifference = (currentMinute - entry.getMinute()) * 60 + (currentSecond - entry.getSecond());

            // Remove entries older than 30 seconds
            if (timeDifference > 60) {
                iterator.remove();
            } else if (entry.getStudentCode().equals(getStudent.getRfid())) {
                // Increase tap count for this student
                entry.incrementTapCount();

                if (entry.getTapCount() >= 3 && timeDifference <= 60) {
                    // Anomaly detected: 3rd tap within 30 seconds
                    securityAlerts.setAnomaly("ID Spamming (3 taps within 30 seconds)");
                    securityAlerts.setTime(dateTime.getCurrentTime());
                    securityAlerts.setDate(dateTime.getCurrentDate());
                    Long hasNotif = 1L;
                    Long counterNotif = notifications.getHasNotif()+hasNotif;
                    notifications.setHasNotif(counterNotif);
                    securityAlertsService.createSecurityAlerts(getStudent, securityAlerts);
                    System.out.println("Anomaly detected: Student tapped 3 times within 30 seconds.");
                    serialService.sendCommand("close");
                    notificationsService.updateNotifications(notifications.getId(), notifications);
                    
                    return "redirect:/smartgate?errorMessage=Anomaly detected: Same student tapped 3 times in a short period of time!";
                }
                foundThirdTap = true;
            }
        }

        // Add new tap to the session array if it's the first or second tap
        if (!foundThirdTap) {
            studentEntries.add(new StudentEntry(getStudent.getRfid(), currentMinute, currentSecond));
        }

        // Normal login flow
        if (getStudent.getLoggedIn() == 0 || getStudent.getLoggedIn() == null) {
            serialService.sendCommand("open");
            logs.setLogType(1);
            logs.setDate(dateTime.getCurrentDate());
            logs.setTime(dateTime.getCurrentTime());
            logsService.createLogs(getStudent, logs);
            studentsService.updateLoggedType(getStudent.getId(), 0);
            session.setAttribute("student", getStudent);
            //serialService.startListening(session);

            try {
                smsApi.sendSms("+63" + getStudent.getPhoneNumber());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unauthorized alert!!");
            serialService.sendCommand("close");
            securityAlerts.setAnomaly("Entry Without Exit Log");
            securityAlerts.setTime(dateTime.getCurrentTime());
            securityAlerts.setDate(dateTime.getCurrentDate());
            securityAlertsRepository.save(securityAlerts);
            
            Long hasNotif = 1L;
            Long counterNotif = notifications.getHasNotif()+hasNotif;
            notifications.setHasNotif(counterNotif);
            notificationsService.updateNotifications(notifications.getId(), notifications);
            securityAlertsService.createSecurityAlerts(getStudent, securityAlerts);
            return "redirect:/smartgate?errorMessage=cant access smartgate";
        }

        // Generate success message and redirect
        String successMessage = String.format("%s %s %s\n%s %s%s\n%s",
            getStudent.getFirstname(),
            getStudent.getMiddlename(),
            getStudent.getLastname(),
            getStudent.getProgramCode(),
            getStudent.getYearLevel(),
            getStudent.getSection(),
            dateTime.getCurrentTime());

        return "redirect:/smartgate?successMessage=" + successMessage + "&&imgUrl=" + "/displayStudent?id=" + getStudent.getId() + "&&time=" + dateTime.getCurrentDay();
    }*/
	    @PostMapping("/enterSmartGate")
	    public ResponseEntity<Map<String, String>> validateStudentMethod(
	            @RequestParam(name = "rfid") String rfid,
	            @RequestParam(name = "toggleState2") String toggleState,
	            HttpSession session) {

	        Map<String, String> response = new HashMap<>();
	        DateTime dateTime = new DateTime();
	        LocalTime now = LocalTime.now();
	        SecurityAlerts securityAlerts = new SecurityAlerts();
	        
	        // Retrieve notifications once to avoid redundant lookups
	        Notifications notifications = notificationsRepository.getByAlertType("securityalerts");

	        // Check if SmartGate is off
	        if ("OFF".equalsIgnoreCase(toggleState)) {
	            return ResponseEntity.badRequest().body(Map.of("errorMessage", "THE SMARTGATE IS OFF, PLEASE TURN IT ON!"));
	        }

	        // Fetch the student by RFID
	        Students getStudent = studentsService.getStudentsByRfid(rfid);
	        if (getStudent == null) {
	            handleUnknownRfid(rfid, dateTime, securityAlerts, notifications);
	            serialService.sendCommand("close");
	            return ResponseEntity.badRequest().body(Map.of("errorMessage", "RFID is unknown, can't access smartgate"));
	        }

	        // Handle session entries
	        @SuppressWarnings("unchecked")
	        List<StudentEntry> studentEntries = (List<StudentEntry>) session.getAttribute("studentEntries");
	        if (studentEntries == null) {
	            studentEntries = new ArrayList<>();
	            session.setAttribute("studentEntries", studentEntries);
	        }

	        // Detect anomalies based on tap count
	        if (checkForAnomalies(getStudent, now, studentEntries, securityAlerts, dateTime, notifications)) {
	            return ResponseEntity.badRequest().body(Map.of("errorMessage", "Anomaly detected: Same student tapped 3 times in a short period of time!"));
	        }

	        // Handle normal login flow
	        if (handleStudentLogin(getStudent, dateTime)) {
	            serialService.sendCommand("open");
	            logStudentEntry(getStudent, dateTime);
	        } else {
	            handleUnauthorizedAccess(securityAlerts, dateTime, notifications, getStudent);
	            serialService.sendCommand("close");
	            return ResponseEntity.badRequest().body(Map.of("errorMessage", "cant access smartgate"));
	        }

	        // Prepare response data and update session
	        prepareResponse(response, getStudent, dateTime);
	        session.setAttribute("entrants", response);
	        updateEntrants(session);

	        return ResponseEntity.ok(response);
	    }

	    private void handleUnknownRfid(String rfid, DateTime dateTime, SecurityAlerts securityAlerts, Notifications notifications) {
	        securityAlerts.setAnomaly("Unknown RFID: " + rfid);
	        securityAlerts.setTime(dateTime.getCurrentTime());
	        securityAlerts.setDate(dateTime.getCurrentDate());
	        securityAlertsRepository.save(securityAlerts);

	        // Update notifications
	        incrementNotificationCount(notifications);
	        securityAlertsService.createSecurityAlerts(null, securityAlerts);
	    }

	    private boolean checkForAnomalies(Students student, LocalTime now, List<StudentEntry> studentEntries, SecurityAlerts securityAlerts, DateTime dateTime, Notifications notifications) {
	        int currentMinute = now.getMinute();
	        int currentSecond = now.getSecond();

	        Iterator<StudentEntry> iterator = studentEntries.iterator();
	        while (iterator.hasNext()) {
	            StudentEntry entry = iterator.next();

	            // Ensure that minutes and seconds are within valid ranges
	            int entryMinute = entry.getMinute();
	            int entrySecond = entry.getSecond();
	            
	            // Add validation for minute and second to ensure they are in valid range
	            if (entryMinute < 0 || entryMinute > 59 || entrySecond < 0 || entrySecond > 59) {
	                System.out.println("Invalid time entry detected: " + entryMinute + ":" + entrySecond);
	                continue; // Skip this invalid entry
	            }

	            // Calculate time difference using valid time values
	            Long timeDifference = Duration.between(LocalTime.of(0, entryMinute, entrySecond), now).getSeconds();

	            if (timeDifference > 60) {
	                iterator.remove();
	            } else if (entry.getStudentCode().equals(student.getRfid())) {
	                entry.incrementTapCount();

	                if (entry.getTapCount() >= 3 && timeDifference <= 60) {
	                    handleIdSpamming(securityAlerts, dateTime, notifications, student);
	                    return true;
	                }
	            }
	        }

	        // Add new tap if no anomaly detected
	        studentEntries.add(new StudentEntry(student.getRfid(), currentMinute, currentSecond));
	        return false;
	    }


	    private void handleIdSpamming(SecurityAlerts securityAlerts, DateTime dateTime, Notifications notifications, Students student) {
	        securityAlerts.setAnomaly("ID Spamming (3 taps within 30 seconds)");
	        securityAlerts.setTime(dateTime.getCurrentTime());
	        securityAlerts.setDate(dateTime.getCurrentDate());
	        securityAlertsRepository.save(securityAlerts);

	        incrementNotificationCount(notifications);
	        securityAlertsService.createSecurityAlerts(student, securityAlerts);
	    }

	    private boolean handleStudentLogin(Students student, DateTime dateTime) {
	        return student.getLoggedIn() == null || student.getLoggedIn() == 0;
	    }

	    private void logStudentEntry(Students student, DateTime dateTime) {
	        Logs logs = new Logs();
	        logs.setLogType(1);
	        logs.setDate(dateTime.getCurrentDate());
	        logs.setTime(dateTime.getCurrentTime());
	        logsService.createLogs(student, logs);
	        studentsService.updateLoggedType(student.getId(), 0);
	    }

	    private void handleUnauthorizedAccess(SecurityAlerts securityAlerts, DateTime dateTime, Notifications notifications, Students student) {
	        securityAlerts.setAnomaly("Entry Without Exit Log");
	        securityAlerts.setTime(dateTime.getCurrentTime());
	        securityAlerts.setDate(dateTime.getCurrentDate());
	        securityAlertsRepository.save(securityAlerts);

	        incrementNotificationCount(notifications);
	        securityAlertsService.createSecurityAlerts(student, securityAlerts);
	    }

	    private void prepareResponse(Map<String, String> response, Students student, DateTime dateTime) {
	        String successMessage = String.format("%s %s %s\n%s %s%s\n%s",
	                student.getFirstname(),
	                student.getMiddlename(),
	                student.getLastname(),
	                student.getProgramCode(),
	                student.getYearLevel(),
	                student.getSection(),
	                dateTime.getCurrentTime());

	        response.put("successMessage", successMessage);
	        response.put("imgUrl", "/displayStudent?id=" + student.getId());
	        response.put("time", dateTime.getCurrentDay());
	    }

	    private void incrementNotificationCount(Notifications notifications) {
	        notifications.setHasNotif(notifications.getHasNotif() + 1);
	        notificationsService.updateNotifications(notifications.getId(), notifications);
	    }


    @PostMapping("/updateEntrants")
    public void updateEntrants(HttpSession session) {
        // Fetch the entrants from the session
        @SuppressWarnings("unchecked")
		Map<String, String> entrants = (Map<String, String>) session.getAttribute("entrants");
        
        // Check if entrants is not null
        if (entrants != null) {
            // Send the updated entrants to the client
            messagingTemplate.convertAndSend("/topic/entrants", entrants);
        } else {
            System.out.println("No entrants found in the session.");
        }
    }
  

    // Updated StudentEntry class to store in session
    class StudentEntry {
        private String studentCode;
        private int minute;
        private int second;
        private int tapCount;

        public StudentEntry(String studentCode, int minute, int second) {
            this.studentCode = studentCode;
            this.minute = minute;
            this.second = second;
            this.tapCount = 1;  // Initial tap
        }

        public String getStudentCode() {
            return studentCode;
        }

        public int getMinute() {
            return minute;
        }

        public int getSecond() {
            return second;
        }

        public int getTapCount() {
            return tapCount;
        }

        public void incrementTapCount() {
            this.tapCount++;
        }
    }

    @PostMapping("/exitSmartGate")
    @ResponseBody
    public String validateStudentExitMethod(
            @RequestParam(name="rfid1") String rfid1,
            @RequestParam(name="rfid2") String rfid2,
            @RequestParam(name="rfid1Date", required=false) String rfid1Date,
            @RequestParam(name="rfid1Time", required=false) String rfid1Time,
            @ModelAttribute Students students,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        System.out.println("RFIDs received: rfid1=" + rfid1 + ", rfid2=" + rfid2);
        System.out.println("RFID1 Date: " + rfid1Date + ", Time: " + rfid1Time);
        
        DateTime dateTime = new DateTime();
        
        // Check if both RFID values are the same
        if (rfid1 != null && rfid2 != null && rfid1.equals(rfid2)) {
            System.out.println("Error: Same RFID entered for both fields.");
            Students getStudent1 = studentsService.getStudentsByRfid(rfid1);
            
            if (getStudent1 != null) {
                Logs logs = new Logs();
                logs.setLogType(0);
                logs.setDate(rfid1Date != null ? rfid1Date : dateTime.getCurrentDate()); // Use date from front end or current date
                logs.setTime(rfid1Time != null ? rfid1Time : dateTime.getCurrentTime()); // Use time from front end or current time
                logsService.createLogs(getStudent1, logs);
                studentsService.updateLoggedType(getStudent1.getId(), 0);
            }
            return "error";
        }
        
        // Fetch the student details based on RFID
        Students getStudent1 = studentsService.getStudentsByRfid(rfid1);
        Students getStudent2 = studentsService.getStudentsByRfid(rfid2);
        
        if (getStudent1 != null || getStudent2 != null) {
            if (getStudent1 != null) {
                Logs logs = new Logs();
                logs.setLogType(0);
                logs.setDate(rfid1Date != null ? rfid1Date : dateTime.getCurrentDate()); // Use date from front end or current date
                logs.setTime(rfid1Time != null ? rfid1Time : dateTime.getCurrentTime()); // Use time from front end or current time
                logsService.createLogs(getStudent1, logs);
                studentsService.updateLoggedType(getStudent1.getId(), 0);
                System.out.println("First RFID processed and log created.");
            }
            
            if (getStudent2 != null) {
                Logs logs = new Logs();
                logs.setLogType(0);
                logs.setDate(dateTime.getCurrentDate()); // Use current date and time for the second RFID
                logs.setTime(dateTime.getCurrentTime()); // Use current date and time for the second RFID
                logsService.createLogs(getStudent2, logs);
                studentsService.updateLoggedType(getStudent2.getId(), 0);
                System.out.println("Second RFID processed and log created.");
            }
            
            return "success";
        } else {
            return "error";
        }
    }






    @PostMapping("/manualOpenClose")
    @ResponseBody
    public String manualOpenClosedMethod(@RequestParam(name = "command") String command, HttpServletRequest request, HttpSession session) {
        System.out.println("commmandddd: " + command);

        if (command != null) {
            if (command.equals("open")) {
                System.out.println("Gate opened");
                serialService.sendCommand("manualOpen");
           
                return "Gate is now open";
            } else if (command.equals("close")) {
                System.out.println("Gate closed");
                serialService.sendCommand("manualClose");
                return "Gate is now closed";
            }
        }

        try {
            Thread.sleep(500); // Wait for Arduino to process and respond
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Command not recognized";
    }

    @GetMapping("/getRfid")
    public String getRfid(Model model) {
    	return "rfid";
    }
    @GetMapping("/getCamera")
    public String getCamera(Model model) {
    	return "camera";
    }
    @PostMapping("/rfidRegister")
    public String rfidRegisterMethod(@ModelAttribute Students student, HttpSession session, Model model, @RequestParam("image")MultipartFile file) throws IOException, SQLException {
    	Students getStudent = studentsService.getStudentsByRfid(student.getRfid());
    	if (getStudent!=null) {
    		return "redirect:/getRfid?errorMessage=already registered!!";
    	}
    	DateTime dateTime = new DateTime();
    	byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

    	String url = apiLoginUrl + "?txtUserName=" + student.getUsername() + "&txtPassword=" + student.getPassword();
        StudentsData studentData = apiAuthenticationService.authenticate(url);
        System.out.println("rfid: "+student.getRfid());
        if (studentData != null && studentData.isLogin()) {
        	student.setLoggedIn(0);
        	student.setUsername(student.getUsername());
        	student.setPassword(student.getPassword());
        	student.setFirstname(studentData.getFirst_name());
        	student.setMiddlename(studentData.getMiddle_name());
        	student.setLastname(studentData.getLast_name());
            student.setYearLevel(String.valueOf(studentData.getYear_level()));
            student.setSection(studentData.getSection());
            student.setProgramCode(studentData.getProgram_code());
            student.setAddress(studentData.getAddress());
            student.setPhoneNumber(studentData.getCp_number());
            student.setCreatedAt(dateTime.getCurrentDateTime());
            student.setRfid(student.getRfid());
            student.setStudentsPic(blob);
            studentsService.createStudents(student);
        	
        }else {
        	 return "redirect:/getRfid?errorMessage=not registered!!";
        }
        	
       return "redirect:/getRfid?successMessage=registered";
    }
//    @GetMapping("/getSms")
//    public String getSms() {
//    	return "aha";
//    }
//    @GetMapping("/getNumber")
//    @ResponseBody
//    public String getPhoneNumber() {
//        try {
//            // Retrieve phone number from your data source
//            String phoneNumber = "09166249209"; // Example number
//            return phoneNumber;
//        } catch (Exception e) {
//            e.printStackTrace(); // Log the exception
//            return "Error retrieving phone number";
//        }
//    }
    @PostMapping("/onOffSmartgate")
    public String onOffSmartgateMethod(HttpServletRequest request, Model model, HttpSession session) {
    	String toggleState = request.getParameter("toggleState");
    	System.out.println("toggleState: "+toggleState);
    	if (toggleState.equalsIgnoreCase("ON")) {
    		serialService.sendCommand("ON");
    		
    	}else {
    		serialService.sendCommand("OFF");
    	}
    	session.setAttribute("toggleState", toggleState);
    	return "redirect:/smartgate"; 
    }
    

}
