package com.smartgate.main.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartgate.main.DateTime;
import com.smartgate.main.entity.ActivityLogs;
//
//import com.smartgate.main.entity.Role;
import com.smartgate.main.entity.Logs;
import com.smartgate.main.entity.Notifications;
import com.smartgate.main.entity.SecurityAlerts;
import com.smartgate.main.entity.Students;
import com.smartgate.main.entity.UserEntity;
import com.smartgate.main.entity.Visitors;
import com.smartgate.main.repository.LogsRepository;
import com.smartgate.main.repository.NotificationsRepository;
//import com.smartgate.main.repository.RoleRepository;
import com.smartgate.main.repository.StudentsRepository;
import com.smartgate.main.repository.UserRepository;
import com.smartgate.main.repository.VisitorsRepository;
import com.smartgate.main.service.ActivityLogsService;
import com.smartgate.main.service.LogsService;
import com.smartgate.main.service.NotificationsService;
import com.smartgate.main.service.SecurityAlertsService;
import com.smartgate.main.service.SerialCommunicationService;
import com.smartgate.main.service.StudentsService;
import com.smartgate.main.service.UserService;
import com.smartgate.main.service.VisitorsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class UsersController {
//	static {
//	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//	}

	


    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogsService logsService;
    @Autowired
    private SecurityAlertsService securityAlertsService;
    @Autowired
    private StudentsService studentsService;
    @Autowired
    private StudentsRepository studentsRepository;
    @Autowired
    private VisitorsRepository visitorsRepository;
    @Autowired
    private VisitorsService visitorsService;
    @Autowired
    private SerialCommunicationService serialService;
    @Autowired
	private NotificationsRepository notificationsRepository;
    @Autowired
	private NotificationsService notificationsService;
    @Autowired
	private ActivityLogsService activityLogsService;

    @Autowired
    private LogsRepository logsRepository;
//    @Autowired
//
//    @GetMapping("/register")
//    public String showRegistration(Model model) {
//        return "registration";
//    }
    @GetMapping("/SmartGateHome")
    public String getHomePage(Model model) {
    	return "home";
    }
    @GetMapping("/app/startingpoint/getSuperRegister")
    public String getSuperRegister(Model model) {
        long userCount = userRepository.count(); // Count the number of users in the repository

        if (userCount == 0) { // Check if there are no users
            return "registration"; // Redirect to the registration page
        } else {
            return "redirect:/"; // Change to the appropriate view if users exist
        }
    }

    @PostMapping("/app/startingpoint/superAdminRegister")
    public String sadminRegister(HttpServletRequest request, Model model, HttpSession session,
    							@RequestParam("image")MultipartFile file) throws IOException, SQLException {
    	String defaultUsername = "smartgatebccsuperadmin";
    	String defaultPassword = "@Superadminbccsmartgate123";
    	String fullname = request.getParameter("fullname");
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	
    	UserEntity user = new UserEntity();
    	byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
    	if (username.equals(defaultUsername)&&password.equals(defaultPassword)) {
    		user.setFullname(fullname);
    		user.setUsername(username);
    		user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
    		user.setStatus(1);
    		user.setImage(blob);
    		user.setUserType("superadmin");
    	}else {
    		return "redirect:/app/startingpoint/getSuperRegister?errorMessage=username or password is not correct";
    	}
    	userService.createUser(user);
    	session.setAttribute("currentUser", user);
    	return "redirect:/getDashboard?successMessage=welcomesuperadmin";
    	
    }
    @PostMapping("/addUsers")
    public ResponseEntity<String> registration(HttpServletRequest request, @RequestParam("image")MultipartFile file, HttpSession session) throws IOException, SQLException {
        // Check if username already exists
    	try {
	        DateTime dateTime = new DateTime();
	        String userType;
	        int status = 1;
	        UserEntity user = new UserEntity();
	        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
	        String fullname = request.getParameter("fullname");
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        String programCode = request.getParameter("programCode");
	        String userTypeForm = request.getParameter("userType");
	        System.out.println("program code code : "+programCode);
	        String contactNumber = request.getParameter("number");
	    	String email = request.getParameter("email");
	        UserEntity findByProgramCode = null; 
	        ActivityLogs activityLogs = new ActivityLogs();
            String startTime = dateTime.getCurrentDateTime();
            activityLogs.setActivity("Adding user.. name of user: "+fullname);
    		activityLogs.setTimeOfActivity(startTime);
    		activityLogsService.createActivityLogs(currentUser, activityLogs);
	        List<UserEntity> getDeptHeads = userRepository.findByUserType("departmenthead");
	        int deptHeadsCount = getDeptHeads.size();
	        if (userTypeForm.equals("departmenthead")) {
	        	if (deptHeadsCount >= 4) {
	        		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				               .body("there are 4 departmenthead accoutns already"); 
	        	}
	        }
	        if (userTypeForm.equals("departmenthead")&&programCode.equals("N/A")) {
	        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			               .body("DEPARTMENTHEAD UNAVAILABLE"); 
	        }
	        if (programCode.equals("ARTS")||programCode.equals("EDUC")||programCode.equals("CRIM")||programCode.equals("IS")) {
	        	findByProgramCode = userRepository.findByProgramCode(programCode);
	        	if (findByProgramCode!=null) {
	        		System.out.println("account with the same program code already exist");
		    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			               .body("User with the program code"+programCode+"is already existed."); 
	        	}else {
	        		System.out.println("program code hasnt existed yyet..");
	        		
	        	}
	        }
//	        if (!programCode.equals(" ")||!programCode.equals("")) {
//	        	
//	          	boolean exists = false;
//	        	if (findByProgramCode!=null) {
//	        		exists = true;
//	        	}
//			     if (exists) {
//			    	 System.out.println("account with the same program code already exist");
//			    	 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//			                    .body("User with the program code"+programCode+"is already existed."); 
//			     } else {
//			    	 user.setProgramCode(programCode != null ? programCode : "N/A");
//			         // Handle the case when no user with the same programCode exists
//			         System.out.println("No users with programCode " + programCode + " found.");
//			     }
//	        }
	        byte[] bytes = file.getBytes();
	        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
	        user.setFullname(fullname);
	        user.setUsername(username);
	        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
	        user.setCreatedAt(dateTime.getCurrentDateTime());
	        user.setImage(blob);
	        user.setStatus(status);
	        user.setUserType(userTypeForm);
	        user.setProgramCode(programCode);
	        user.setEmail(email);
	        user.setContactNumber(contactNumber);
	    	userService.createUser(user);
	    	return ResponseEntity.ok("User successfully added");
    	}catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding user: " + e.getMessage());
    	}
    }
    
    @PostMapping("/profUpdate")
    public String profileUpdateMethod(HttpServletRequest request, HttpSession session, @RequestParam("image") MultipartFile file) throws IOException, SQLException {
    	
    	DateTime dateTime = new DateTime();
    	ActivityLogs activityLogs = new ActivityLogs();
        String id = request.getParameter("id");
        String fullname = request.getParameter("fullname");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");  // Ensure this matches the form field name
        String startTime = dateTime.getCurrentDateTime();
		
        UserEntity getCurrentUser = userService.getUserById((long) Integer.parseInt(id));
        System.out.println(userType);  // Check if the value is correct
		activityLogs.setActivity("Profile update");
		activityLogs.setTimeOfActivity(startTime);
		activityLogsService.createActivityLogs(getCurrentUser, activityLogs);
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        getCurrentUser.setFullname(fullname);
        getCurrentUser.setUsername(username);
        getCurrentUser.setPassword(password);
        getCurrentUser.setImage(blob);
        getCurrentUser.setUserType(userType);
        session.setAttribute("currentUser", getCurrentUser);
        userService.updateUser(getCurrentUser);
        return "redirect:/getProfile";
    }

    @GetMapping("/check-username")
    @ResponseBody
    public boolean checkUsername(@RequestParam("username") String username) {
        UserEntity existingUser = userService.getUserByUsername(username);
        return existingUser!=null; // Returns true if user exists, false otherwise
    }
    @GetMapping("/check-programCode")
    @ResponseBody
    public boolean checkProgramCode(@RequestParam("programCode") String programCode) {
    	System.out.println("programCode:"+programCode);
        UserEntity existingCode = userRepository.findByProgramCode(programCode);
        return existingCode!=null; // Returns true if user exists, false otherwise
    }

//    @GetMapping("/departmenthead/aw")
//    public String departmentHeadHome() {
//        // Return the view name for the department head dashboard
//        return "aha"; // Make sure this corresponds to a template in your resources/templates folder
//    }

    @GetMapping
    public String showLogin(Model model, HttpSession session) {
    	UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
    	if (currentUser!=null) {
    		//return "redirect:/?errorMessage=theres a current user existed in the session, please logout in order to log new account";
    		return "redirect:/getDashboard";
    	}
        return "login";
    }
    
    @GetMapping("/getProfile")
    public String getProfile(Model model, HttpSession session) {
    	String isLogged = (String) session.getAttribute("isLogged");
    	UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
    	
    	if (isLogged == null || currentUser == null) {
    		System.out.println("Not signed in!");
    		return "redirect:/?unauthorizedAccess=You are not logged in.";
    		
    	}
    	System.out.println("currentUSER: "+currentUser.getFullname()+currentUser.getUsername());;
    	model.addAttribute("currentUser", currentUser);
    	return "profile";
    	
    }
    @PostMapping("/signIn")
    public String login(@ModelAttribute UserEntity user, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
    	UserEntity existingAdminByUsername = userService.getUserByUsername(user.getUsername());
    	boolean realPassword = true;
    	ActivityLogs activityLogs = new ActivityLogs();
    	DateTime dateTime = new DateTime();
    	UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
    	if (currentUser!=null) {
    		return "redirect:/?errorMessage=theres a current user existed in the session, please logout in order to log new account";
    	}
    	if (existingAdminByUsername!=null) {
        	realPassword =  BCrypt.checkpw(user.getPassword(), existingAdminByUsername.getPassword());
    	}

    	Long getStudentsCount = studentsRepository.count();
    	Long getVisitorsCount = visitorsRepository.count();
        if (existingAdminByUsername!=null&&realPassword) {
        	session.setAttribute("isLogged", "isLogged");
        	session.setAttribute("getStudentsCount", getStudentsCount);
        	session.setAttribute("getVisitorsCount", getVisitorsCount);
        	session.setAttribute("currentUser", existingAdminByUsername);
//        	if (existingAdminByUsername.getUserType().equals("guard")) {
//        		return "guardDashboard";
//        	}
//        	if (existingAdminByUsername.getUserType().equals("departmenthead")) {
//        		return "redirect:/departmentHeadDashboard";
//        	}
        	if (existingAdminByUsername.getStatus()==0) {
        		redirectAttributes.addFlashAttribute("credentialsError", "Incorrect username or password.");
        		 return "redirect:/";
        	}
        	String startTime = dateTime.getCurrentDateTime();
        	List<ActivityLogs> getGuardIsLogged = activityLogsService.getByIsLoggedAndUserType(1, "guard");
        	if (existingAdminByUsername.getUserType().equals("guard")) {

	        		for(ActivityLogs getGuard : getGuardIsLogged) {
	            		if (getGuard.getUsers().getUsername().equals(existingAdminByUsername.getUsername())) {
	            			getGuard.setIsLogged(1);
	            			activityLogsService.updateActivityLogs(getGuard.getId(), getGuard);
	            		}else {
	            			getGuard.setIsLogged(0);
	            			activityLogsService.updateActivityLogs(getGuard.getId(), getGuard);
	            			
	            		}
	        		}
	        		activityLogs.setStartTimeStamp(startTime);
            		activityLogs.setIsLogged(1);
            		activityLogs.setActivity("Log-in");
            		activityLogs.setTimeOfActivity(startTime);
            		activityLogsService.createActivityLogs(existingAdminByUsername, activityLogs);
        		session.setAttribute("startTime", startTime);
        		session.setAttribute("currentGuardUser", existingAdminByUsername);
        		System.out.println(existingAdminByUsername.getUserType()+"badddddssssssss");
        	}else {
        		activityLogs.setIsLogged(1);
        		activityLogs.setActivity("Log-in");
        		activityLogs.setTimeOfActivity(startTime);
        		activityLogsService.createActivityLogs(existingAdminByUsername, activityLogs);
        	}
        	
        	//if (existingAdminByUsername.getUsername().equals(getGuardIsLogged))
        
        		

        	System.out.println(existingAdminByUsername.getUserType());
        	return "redirect:/getDashboard";
        } else {
            redirectAttributes.addFlashAttribute("credentialsError", "Incorrect username or password.");
        }
        return "redirect:/";
    }

//    	
////    }
//    @PostMapping("/signIn")
//    public String loginG(@ModelAttribute UserEntity user, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // Retrieve the authenticated user
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            // Print the roles of the authenticated user
//            userDetails.getAuthorities().forEach(authority -> {
//                System.out.println("User Role: " + authority.getAuthority());
//            });
//
//            // Redirect or return the view based on the authentication success
//            return "redirect:/departmenthead/dashboard"; // Redirect to a home page or dashboard after successful login
//
//        } catch (Exception e) {
//            model.addAttribute("error", "Invalid username or password");
//            return "login"; // Return to login page with error message
//        }
//    }



    @GetMapping("/getUsers")
    public String getEnabledUsers(Model model, HttpSession session) {
    	String isLogged = (String) session.getAttribute("isLogged");
    	UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
    	if (currentUser == null) {
    		System.out.println("Not signed in!");
    		return "redirect:/?unauthorizedAccess=You are not logged in.";
    	}
    	if (currentUser.getUserType().equals("admin")){
    		String[] userTypes = {"admin", "superadmin"};
    		List<UserEntity> getUserss = userRepository.findByUserTypeNotInAndUsernameNot(userTypes, currentUser.getUsername());
    		model.addAttribute("users", getUserss);
    	}else {
    		List<UserEntity> getUserss = userService.getByUsertypeAndUsernameNot("superadmin", currentUser.getUsername());
    		model.addAttribute("users", getUserss);
    		
    	}
    	List<ActivityLogs> getGuardIsLogged = activityLogsService.getByIsLoggedAndUserType(1, "guard");
    	List<ActivityLogs> getAllGuardLogs = activityLogsService.getByUserUserType("guard");
    	List<ActivityLogs> getAllActivityLogs = activityLogsService.getByUserTypeNot("superadmin");
        session.setAttribute("guardLogs", getAllGuardLogs);
        session.setAttribute("activityLogs", getAllActivityLogs);
        session.setAttribute("currentUser", currentUser);
        session.setAttribute("loggedGuards", getGuardIsLogged);
        
        return "users";
    }

//    @GetMapping("/getDisabledUsers")
//    public String getDisabledUsers(Model model, HttpSession session) {
//    	String isLogged = (String) session.getAttribute("isLogged");
//    	if (isLogged == null) {
//    		System.out.println("Not signed in!");
//    		return "redirect:/?unauthorizedAccess=You are not logged in.";
//    	}
//        List<UserEntity> disabledAdmins = userService.getUserByStatus(0);
//        model.addAttribute("users", disabledAdmins);
//        return "deletedUsers";
//    }
    @GetMapping("/getDashboard")
    public String getDashboard(Model model, HttpSession session) {

    	UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
    	
        if (currentUser == null) {
            return "redirect:/?unauthorizedAccess=You are not logged in.";
        }
        
        //session.setAttribute("currentUser", currentUser);

        return "dashboard";
        
    }
    @GetMapping("/getProgramCodeLogs")
    public ResponseEntity<List<Map<String, Object>>> getProgramCodeLogs() {
        try {
            List<Object[]> programCodeLogs = logsRepository.findTotalLogsByProgramCode();
            
            // Log raw data for debugging
            System.out.println("Raw programCodeLogs data: " + programCodeLogs.size()+"hahahah");
            
            // Convert List<Object[]> to List<Map<String, Object>>
            List<Map<String, Object>> response = programCodeLogs.stream()
                .map(record -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("programCode", record[0]);
                    map.put("count", record[1]);
                    return map;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/getTotalStudentsByProgramCode")
    public ResponseEntity<List<Map<String, Object>>> totalStudents() {
        try {
            List<Object[]> totalStudents = studentsRepository.countStudentsByProgramCode();
            
            // Log raw data for debugging
            System.out.println("Raw totalStudents data: " + totalStudents);
            
            // Convert List<Object[]> to List<Map<String, Object>>
            List<Map<String, Object>> response = totalStudents.stream()
                .map(record -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("programCode", record[0]);
                    map.put("count", record[1]);
                    return map;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/getTotalStudentsByYearLevel")
    public ResponseEntity<List<Map<String, Object>>> totalStudentsByYearLevel(HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser==null) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        try {
            List<Object[]> totalStudents = studentsRepository.countByProgramCodeGroupByYearLevel(currentUser.getProgramCode());

            // Log raw data for debugging
            System.out.println("Raw totalStudents data: " + totalStudents);

            // Convert List<Object[]> to List<Map<String, Object>>
            List<Map<String, Object>> response = totalStudents.stream()
                .map(record -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("yearLevel", record[0].toString()); // Use a lowercase key for consistency
                    map.put("count", ((Number)record[1]).intValue()); // Ensure count is treated as an int
                    return map;
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getTotalVisitorsInCampus")
    public ResponseEntity<Map<String, Long>> totalVisitorsInCampus() {
        try {
            Long totalVisitorsInCampus = visitorsRepository.countLoggedInVisitors();
            Long totalVisitorsOutCampus = visitorsRepository.countVisitorsNotInCampus();
            // Log raw data for debugging
            System.out.println("total visitors incampus: "+totalVisitorsInCampus);
            System.out.println("total visitors Not incampus: "+totalVisitorsOutCampus);
            Map<String, Long> visitorCounts = new LinkedHashMap<>();
            visitorCounts.put("currentVisitorsInCampus", totalVisitorsInCampus);
            visitorCounts.put("visitorsNotInCampus", totalVisitorsOutCampus);
            
            return ResponseEntity.ok(visitorCounts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/getTotalVisitors")
    public ResponseEntity<Map<String, Long>> totalVisitors() {
        try {
        	Map<String, Long> totalVisitors = visitorsService.getVisitorCounts();
            // Log raw data for debugging
            System.out.println("Raw totalStudents data: " + totalVisitors);
            

            
            return ResponseEntity.ok(totalVisitors);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/getTotalVisitorsLogs")
    public ResponseEntity<Integer> totalVisitorsAndVisitorsLogs() {
        try {
        	List<Logs> totalVisitorLogs = logsRepository.findAllLogsWithVisitor();
        	int visitorLogsCount = totalVisitorLogs.size();
            // Log raw data for debugging
            System.out.println("Raw totalStudents data: " + visitorLogsCount);
            

            
            return ResponseEntity.ok(visitorLogsCount);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/getTotalStudentsLogs")
    public ResponseEntity<Integer> totalStudentsLogs() {
        try {
        	List<Logs> totalStudentLogs = logsRepository.findAllLogsWithStudent();
        	int studentLogsCount = totalStudentLogs.size();
            // Log raw data for debugging
            System.out.println("Raw totalStudents data: " + studentLogsCount);
            

            
            return ResponseEntity.ok(studentLogsCount);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    


    @GetMapping("/getStudents")
    public String getStudents(Model model, HttpSession session) {
    	String isLogged = (String) session.getAttribute("isLogged");
        if (isLogged == null) {
            return "redirect:/?unauthorizedAccess=You are not logged in.";
        }
        List<Students> getAllStudents = studentsService.getAllStudents();
        model.addAttribute("students", getAllStudents);
        return "students";
        
    }
    
    @PostMapping("/updateUser")
    @ResponseBody
    public String updateUser(@RequestParam("id") Long id,
                             @RequestParam("fullname") String fullname,
                             @RequestParam("username") String username,
                             @RequestParam("contactNumber") String contactNumber,
	                         @RequestParam(value = "gender", required = false) String gender,
	                         @RequestParam(value = "programCode", required = false) String programCode,
	                         @RequestParam(value = "userType", required = false) String userType,
	                         @RequestParam("email") String email,
	                         @RequestParam("status") int status,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             HttpSession session) {
        try {
        	UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
            UserEntity existingAdmin = userService.getUserById(id);
            ActivityLogs activityLogs = new ActivityLogs();
            DateTime dateTime = new DateTime();
           
            if (existingAdmin == null) {
                return "error: user not found";
            }
            String startTime = dateTime.getCurrentDateTime();
            activityLogs.setActivity("Updating user.. name of user: "+existingAdmin.getFullname());
    		activityLogs.setTimeOfActivity(startTime);
    		activityLogsService.createActivityLogs(currentUser, activityLogs);
    		int userStatus = existingAdmin.getStatus();
    		String statusType = (userStatus == 1) ? "disabling" 
    	              : (userStatus == 0) ? "enabling"
    	              :" ";
    		if (userStatus!=status) {
    			activityLogs.setActivity(statusType+" user .. name of user: "+existingAdmin.getFullname());
        		activityLogs.setTimeOfActivity(startTime);
        		activityLogsService.createActivityLogs(currentUser, activityLogs);
    		}
//            System.out.println("aw ahahahahha"+existingAdmin.getUserType()+programCode);
//            // Check for department head and set program code
//            if ("departmenthead".equals(existingAdmin.getUserType())) {
//                existingAdmin.setProgramCode(programCode != null ? programCode : "");
//            }

            // Update the rest of the fields
            existingAdmin.setContactNumber(contactNumber != null ? contactNumber : "");
            existingAdmin.setGender(gender != null ? gender : "");
            existingAdmin.setFullname(fullname != null ? fullname : "");
            existingAdmin.setUsername(username != null ? username : "");
            existingAdmin.setUserType(userType != null ? userType : "");
            if (userType.equals("admin")||userType.equals("guard")) {
            	existingAdmin.setProgramCode("N/A");
            }else {
            	existingAdmin.setProgramCode(programCode != null ? programCode : "");
            }
            
            existingAdmin.setEmail(email != null ? email : "");
            existingAdmin.setContactNumber(contactNumber != null ? contactNumber : "");
            existingAdmin.setStatus(status);
//            existingAdmin.setUserType(userType != null ? userType : "");

            // Handle image upload
            if (image != null && !image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
                existingAdmin.setImage(blob);
            }

            userService.updateUser(existingAdmin);
            return "success";

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return "error: exception occurred";
        } catch (NullPointerException e) {
            return "error: null pointer exception";
        }
    }



    @GetMapping("/display")
    public ResponseEntity<byte[]> displayImage(@RequestParam long id) throws IOException, SQLException {
        UserEntity user = userService.getUserById(id);
        
        // Check if the user or the image is null
        if (user == null || user.getImage() == null) {
            // Return 404 Not Found if the user or the image is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] imageBytes = user.getImage().getBytes(1, (int) user.getImage().length());
        
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @GetMapping("/displayStudent")
    public ResponseEntity<byte[]> displayImageStudent(@RequestParam long id) throws IOException, SQLException {
    	Students image = studentsService.getStudentById(id);
    	byte[] imageBytes = null;
        imageBytes = image.getStudentsPic().getBytes(1, (int) image.getStudentsPic().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);

       
        
    }
    @GetMapping("/displayVisitor")
    public ResponseEntity<byte[]> displayImageVisitor(@RequestParam Long id) {
        Visitors visitor = visitorsService.getVisitorById(id);
        if (visitor == null || visitor.getImage() == null) {
            return ResponseEntity.notFound().build(); // Handle null or not found case
        }

        Blob imageBlob = visitor.getImage();
        try {
            InputStream inputStream = imageBlob.getBinaryStream();
            byte[] imageBytes = inputStream.readAllBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Or appropriate media type
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/softDeleteUser")
    public String softDeleteUserMethod(UserEntity user /*@RequestParam("formType") String formType*/) throws IOException, SQLException {
    	UserEntity getUser = userService.getUserById(user.getId());
        if (getUser != null) {
            int softDelete = 0;
            getUser.setStatus(softDelete);
            userService.updateUser(getUser);
        }
//        if (formType == "1") {
//        	return "redirect:/";
//        }
        return "redirect:/getUsers"; // Use redirect to avoid form resubmission issues
    }
    @PostMapping("/hardDeleteUser")
    public String hardDeleteUserMethod(@RequestParam("id") Long id, /*@RequestParam("formType") String formType*/ RedirectAttributes redirectAttributes) throws IOException, SQLException {
    	UserEntity getUser = userService.getUserById(id);
        try {
	        if (getUser != null) {
	        	userService.deleteById(id);
	            
	            redirectAttributes.addFlashAttribute("message", "User deleted successfully.");
	        } else {
	            redirectAttributes.addFlashAttribute("error", "User not found.");
	        }
	        return "success"; // Use redirect to avoid form resubmission issues
        }catch(Exception e) {
        	return "error on deleting user"+e.getMessage();
        }
    }

    @PostMapping("/restoreDeletedUser")
    public String restoreDeletedUserMethod(UserEntity user) throws IOException, SQLException {
    	UserEntity getUser = userService.getUserById(user.getId());
        if (getUser != null) {
            int restore = 1;
            getUser.setStatus(restore);
            userService.updateUser(getUser);
        }
        return "redirect:/getUsers"; // Use redirect to avoi                                                     d form resubmission issues
    }

//    @GetMapping("/smartgate")
//    public String getSmartGate(Model model, HttpSession session) {
//        String isLogged = (String) session.getAttribute("isLogged");
//        if (isLogged == null) {
//            System.out.println("Not signed in!");
//            return "redirect:/?unauthorizedAccess=You are not logged in.";
//        }
//
//        List<Logs> allLogs = logsRepository.findAll();
//  
//        System.out.println();
////        LocalDate today = LocalDate.now();
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
////
////        // Filter logs based on the current day and month
////        List<Logs> filteredLogs = allLogs.stream()
////            .filter(log -> {
////                try {
////                    LocalDateTime logDateTime = LocalDateTime.parse(log.getDate(), formatter);
////                    LocalDate logDate = logDateTime.toLocalDate();
////                    System.out.println("Parsed log date: " + logDate + ", Day: " + logDate.getDayOfMonth() + ", Month: " + logDate.getMonth());
////                    return logDate.getDayOfMonth() == today.getDayOfMonth() &&
////                           logDate.getMonth() == today.getMonth();
////                } catch (DateTimeParseException e) {
////                    System.err.println("Error parsing date: " + log.getDate());
////                    e.printStackTrace();
////                    return false;
////                }
////            })
////            .collect(Collectors.toList());
//
//        model.addAttribute("logs", allLogs);
//        return "smartgate"; // Thymeleaf template name
//    }
    @GetMapping("/smartgate")
    public String getSmartGate(Model model, HttpSession session) {
//    	String isLogged = (String) session.getAttribute("isLogged");
    	UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/?unauthorizedAccess=You are not logged in.";
        }
        List<Logs> logs = logsService.getAllLogs();
//        Logs logss= logs.get(0);
//        Logs logss2= logs.get(1);
//        if (logss.is)
//        System.out.println(logss.getDate()+logss2.getDate());
        model.addAttribute("logs",logs);
        return "smartgate";
	
		
    	
    }
//    @GetMapping("getLogHistory")
//    public String getLogHistory(Model model, HttpSession session) {
//    	String isLogged = (String) session.getAttribute("isLogged");
//        if (isLogged == null) {
//            return "redirect:/?unauthorizedAccess=You are not logged in.";
//        }
//        List<Logs> logs = logsService.getAllLogs();
//        Logs logss= logs.get(0);
//        Logs logss2= logs.get(1);
//        System.out.println(logss.getDate()+logss2.getDate());
//        model.addAttribute("logs",logs);
//        return "logHistory";
//    }
    @PostMapping("/getVisitorLogs")
    public String visitorLogsMethod(Model model) {
        List<Logs> getAllVisitorsLogs = logsService.getLogsWithVisitors();

            model.addAttribute("logs", getAllVisitorsLogs);
            model.addAttribute("getLog", "visitors");

        
        return "logHistory";
    }
    @PostMapping("/getStudentLogs")
    public String studentsLogsMethod(Model model) {
  
        List<Logs> getAllStudentsLogs = logsService.getLogsWithStudents();
        model.addAttribute("logs", getAllStudentsLogs);
        model.addAttribute("getLog", "students");


        return "logHistory";
    }
    

   
    @PostMapping("/getLogsByProgramCode")
    public String getLogsByProgramCodeMethod (HttpServletRequest request, Model model) {
		String programCode = request.getParameter("programCode");
		List<Logs> getByProgramCode = logsService.getLogsByProgramCode(programCode);
		if (getByProgramCode==null) {
			System.out.println("asfasasfdsasadasdasd");
			return "smartgate";
		}
		model.addAttribute("logs", getByProgramCode);
		return "smartgate";
    	
    }
    @GetMapping("/logout")
    public String logoutMethod(HttpSession session) {
    	ActivityLogs logs = new ActivityLogs();
    	DateTime dateTime = new DateTime();
    	String startTime = (String) session.getAttribute("startTime");
    	UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
    	if (currentUser == null) {
            return "redirect:/?unauthorizedAccess=You are not logged in.";
        }
    	if (startTime!=null) {
	    	ActivityLogs getGuardLog = activityLogsService.getActivityLogsByStartTimeStamp(startTime);
	    	if (currentUser.getUserType().equals("guard")&&getGuardLog!=null) {
	    		
	    		logs.setEndTimeStamp(dateTime.getCurrentDateTime());
	    		logs.setIsLogged(0);
	    		activityLogsService.updateActivityLogs(getGuardLog.getId(), logs);
	    	}
    	}
    	ActivityLogs userActivity = new ActivityLogs();
    	userActivity.setActivity("Logout");
    	userActivity.setTimeOfActivity(startTime);
		activityLogsService.createActivityLogs(currentUser, userActivity);
    	System.out.println("logsout"+session.getAttribute("isLogged"));
    	session.invalidate();
    	
    	return "redirect:/";
    }
    
    @GetMapping("/getVisitors")
    public String getVisitors(Model model, HttpSession session) {
    	List<Visitors> getAllVisitors = visitorsService.getAllVisitors();
    	UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/?unauthorizedAccess=You are not logged in.";
        }
        model.addAttribute("visitors", getAllVisitors);
        return "visitors";
    }
    @GetMapping("/getSecurityAlerts")
    public String securityAlerts(@RequestParam(name="notif")String notif, Model model, HttpSession session) {
    	List<SecurityAlerts> getAllSecurityAlerts = securityAlertsService.getStudentSecurityAlerts();
    	List<SecurityAlerts> getAll = securityAlertsService.getUnknownSecurityAlerts();
    	Notifications getNotif = notificationsRepository.getByAlertType("securityalerts");
    	UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
    	if (notif.equals("hasNotif")) {
    		Long hasNotif = 0l;
    		getNotif.setHasNotif(hasNotif);
    		notificationsService.updateNotifications(getNotif.getId(), getNotif);
    		System.out.println("NotiifCleared");
    	}
        if (currentUser == null) {
            return "redirect:/?unauthorizedAccess=You are not logged in.";
        }
        session.setAttribute("securityAlerts", getAllSecurityAlerts);
        session.setAttribute("getAll", getAll);
        session.setAttribute("currentUser", currentUser);
        return "SecurityAlerts";
    }
    @GetMapping("/getNotifications")
    public ResponseEntity<Notifications> getData(HttpSession session) {
        // Assuming 'notificationsRepository' returns a 'Notifications' object
        Notifications getNotif = notificationsRepository.getByAlertType("securityalerts");
        
        // Save notification status in session (if needed)
        session.setAttribute("notif", getNotif.getHasNotif());
        
        // Log for debugging purposes
        System.out.println("Fetched notification: " + getNotif);
        
        // Return the 'Notifications' object as JSON
        return ResponseEntity.ok(getNotif);
    }

//    @PostMapping("/enterSmartGate") 
//    public String enterSmartGateMethod(@ModelAttribute Admins user, Model model) {
//		List<Admins> userEntered = adminService.getAdminByUsername(user.getUsername());
//		if (!userEntered.isEmpty()) {
//			System.out.println("gate opened");
//			serialService.sendCommand("open");
//	        try {
//	            Thread.sleep(500); // Wait for Arduino to process and respond
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//	        }
//	        return "smartgate";
//		}else {
//			System.out.println("unauthorized alert!!");
//			serialService.sendCommand("close");
//	        try {
//	            Thread.sleep(500); // Wait for Arduino to process and respond
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//	        }
//			return "smartgate";
//		}
//
//
//    	
//    }
    @RequestMapping(value = {"/registration", "/signIn", "/updateUser", "/softDeleteUser", "/hardDeleteUser", "/restoreDeletedUser"}, method = RequestMethod.GET)
    public String handlePostDirectAccess() {
        // Redirect to the registration form if accessed directly
		return "redirect:/";
    }
    
//    @GetMapping("getData")
//    public String getDatas(Model mode, HttpSession session) {
//    	Students getAllStudents = studentsService.getAllStudents();
//    	Students getStudents by
//    }
    
//
//    private CascadeClassifier faceCascade;
//
//    // Initialize the CascadeClassifier in the constructor
//    public AdminController() {
//        // Path to your Haar Cascade XML file (ensure this path is correct)
//        faceCascade = new CascadeClassifier("C:\\Users\\jenie ann aballe\\Desktop\\spring installation\\Try\\SmartGate\\src\\main\\resources\\haarcascade_frontalface_default.xml");
//        // Check if the cascade was loaded correctly
//        if (faceCascade.empty()) {
//            System.err.println("Failed to load Haar Cascade XML file.");
//        }
//    }
//
//
//    @PostMapping("/compare-face")
//    public boolean compareFace(@RequestParam("capturedImage") MultipartFile capturedImage, @RequestParam("adminId") Long id) throws IOException, SQLException {
//        // Fetch stored image as byte[]
//        Admins admin = adminService.getAdminById(54L);
//        Blob imageBlob = admin.getImage();
//        byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
//
//        // Convert byte[] to BufferedImage and then to Mat
//        BufferedImage storedBufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
//        Mat storedImageMat = bufferedImageToMat(storedBufferedImage);
//
//        // Convert captured image to Mat
//        Mat capturedImageMat = bufferedImageToMat(ImageIO.read(capturedImage.getInputStream()));
//
//        // Detect faces and compare them
//        MatOfRect storedFaces = detectFaces(storedImageMat);
//        MatOfRect capturedFaces = detectFaces(capturedImageMat);
//
//        // Continue with face comparison logic...
//
//        return false; // or true based on comparison
//    }
//
//    private Mat bufferedImageToMat(BufferedImage image) throws IOException {
//        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
//        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//        mat.put(0, 0, data);
//        return mat;
//    }
//
//    private MatOfRect detectFaces(Mat image) {
//        MatOfRect faceDetections = new MatOfRect();
//        faceCascade.detectMultiScale(image, faceDetections);
//        return faceDetections;
//    }




}