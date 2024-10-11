package com.smartgate.main.controller;

//
//import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/departmenthead")
public class DepartmentheadController {
	
	@GetMapping("/dashboard")
    public String departmentHeadHome() {
        // Return the view name for the department head dashboard
        return "departmentHeadDashboard"; // Make sure this corresponds to a template in your resources/templates folder
    }
}

