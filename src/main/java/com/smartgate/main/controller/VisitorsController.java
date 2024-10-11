package com.smartgate.main.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.sql.rowset.serial.SerialException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.smartgate.main.DateTime;
import com.smartgate.main.QRCodeGenerator;
import com.smartgate.main.entity.Logs;
import com.smartgate.main.entity.Students;
import com.smartgate.main.entity.StudentsData;
import com.smartgate.main.entity.UserEntity;
import com.smartgate.main.entity.Visitors;
import com.smartgate.main.repository.VisitorsRepository;
import com.smartgate.main.service.LogsService;
import com.smartgate.main.service.SerialCommunicationService;
import com.smartgate.main.service.VisitorsService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/visitors")
public class VisitorsController {
    @Autowired
    private VisitorsService visitorsService;
    @Autowired
    private VisitorsRepository visitorsRepository;
   
    @Autowired
    private SerialCommunicationService serialService;
    @Autowired
	private LogsService logsService;
    @GetMapping("/getVisitorRegistration")
    public String getVisitorRegistration(Model model) {
        return "visitorRegistration";
    }
    @GetMapping("/getQRCode")
    public String getQRCode(Model model) {
        return "QRCodeGenerator";
    }
    
//    @PostMapping("visitorsRegistration")
//    public String visitorRegistrationMethod(@ModelAttribute Visitors visitor, Model model, @RequestParam("image") MultipartFile file) throws IOException {
//        byte[] bytes = file.getBytes();
//        visitor.setImage(bytes); // Directly set byte[]
//        visitorsService.createVisitor(visitor);
//        return "redirect:/getVisitorRegistration";
//    }
    @PostMapping("/visitorsRegistration")
    public String visitorRegistrationMethod(           
            HttpSession session, HttpServletRequest request, 
            Model model) throws IOException, SQLException {
    	String code = request.getParameter("code");
    	Visitors getVisitor = visitorsService.getVisitorByRegistrationCode(code);
    	System.out.println("code code code"+code);
    	if (getVisitor==null) {
    		
    		session.setAttribute("status", "error");
    		return "redirect:/visitors/getVisitorRegistration";
    	}else {
        	


            try {
                // Generate the QR code as byte[]
                byte[] qrCodeImage = QRCodeGenerator.generateQRCode(String.valueOf(code), 200, 200);

                // Define the path to save the image in the static folder (src/main/resources/static/images)
                String fileName = "qr_" + code + ".png";
                Path path = Paths.get("src/main/resources/static/images/" + fileName);
                String base64QrImage = Base64.getEncoder().encodeToString(qrCodeImage);
                // Save the image to the path
                Files.write(path, qrCodeImage);
                System.out.println("fukeubameasd: "+fileName);
                // Add the image path to the model to use it in the frontend
                session.setAttribute("qrCodeImagePath", "/images/"+fileName);
                session.setAttribute("base64QrImage", base64QrImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
    	}
    	System.out.println(getVisitor.getEmail());
        session.setAttribute("status", "success");

        // Redirect to a page with the email and verification code as query parameters
        return "redirect:/visitors/getVisitorRegistration?successMessage=registeredsuccessfully";
    }


    @PostMapping("/resendCode")
    public ResponseEntity<String> resendCodeMethod(@RequestParam("registrationCode") String code, Model model) {
    	System.out.println("hahahahaha"+code);
    	
    	Visitors currentVisitor = visitorsService.getVisitorByRegistrationCode(code);
    	if (currentVisitor!=null) {
    		System.out.println("ididi"+currentVisitor.getId());
    		return new ResponseEntity<>(code, HttpStatus.OK);

        }
    	return new ResponseEntity<>("Error resending code", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam("email") String email) {
    	System.out.println(email+email);
        Visitors visitor = visitorsService.getVisitorByEmail(email);
        return visitor==null; // Returns true if user exists, false otherwise
    }
    @GetMapping("/check-email2")
    @ResponseBody
    public boolean checkEmail2(@RequestParam("email") String email) {
    	System.out.println(email+email);
        Visitors visitor = visitorsService.getVisitorByEmail(email);
        return visitor!=null; // Returns true if user exists, false otherwise
    }
    @GetMapping("/invalidateSession")
    @ResponseBody
    public boolean invalidateSession(HttpSession session) {
    	session.setAttribute("status", "failed");
    	
        return true; // Returns true if user exists, false otherwise
    }
    @PostMapping("/recoverCode")
    public String recoverCodeMethod(@ModelAttribute Visitors visitor, Model model) {
    	Visitors getEmailVisitor = visitorsService.getVisitorByEmail(visitor.getEmail());
    	return "redirect:/visitors/getVisitorRegistration?recoveryCode="+getEmailVisitor.getRegistrationCode()+"&email="+getEmailVisitor.getEmail();
    	
    }
    @PostMapping("/visitorExit")
    public String visitorExit(@RequestParam(name="qrCode") String qrCode, @ModelAttribute Visitors visitor, Model model, HttpSession session) {
    	Logs logs = new Logs();
    	DateTime dateTime = new DateTime();
    	Visitors getVisitor = visitorsService.getVisitorByRegistrationCode(qrCode);
    	
    	if (getVisitor == null) {
    		
            serialService.sendCommand("close");
            return "redirect:/smartgate?errorMessage=cannot recognize visitor";
    	}else {
    		if (getVisitor.getLoggedIn() == 0) {
    			serialService.sendCommand("close");
    			return "redirect:/smartgate?errorMessage=visitor entry unknown!!";
        	}
    		getVisitor.setLoggedIn(0)    	;	
    		logs.setLogType(0);
            logs.setDate(dateTime.getCurrentDate());
            logs.setTime(dateTime.getCurrentTime());
            logsService.createVisitorLogs(getVisitor, logs);
            visitorsService.updateVisitor(getVisitor.getId(), getVisitor);
            session.setAttribute("visitor", getVisitor);
    	}
    	System.out.println(getVisitor.getPurposeOfvisit());
    	 // Generate the success message and redirect
        String successMessage = String.format("%s %s \n%s %s\n%s",
        	getVisitor.getFirstname(),
        	getVisitor.getLastname(),
        	getVisitor.getEmail(),
        	getVisitor.getAddress(),

            dateTime.getCurrentTime());

        return "redirect:/smartgate?successMessage=" + successMessage + "&&imgUrl="+"/displayVisitor?id="+getVisitor.getId()+ "&&time=" + dateTime.getCurrentDay();
    }
    @PostMapping("/visitorAccess") 
    public String visitorAccessMethod(@RequestParam(name="qrCode") String qrCode, @ModelAttribute Visitors visitor, Model model, HttpSession session) {
    	Logs logs = new Logs();
    	DateTime dateTime = new DateTime();
    	Visitors getVisitor = visitorsService.getVisitorByRegistrationCode(qrCode);
    	
    	if (getVisitor == null) {
    		
            serialService.sendCommand("close");
            return "redirect:/smartgate?errorMessage=cant access smartgate";
    	}else {
    		getVisitor.setLoggedIn(1)    	;	
    		logs.setLogType(1);
            logs.setDate(dateTime.getCurrentDate());
            logs.setTime(dateTime.getCurrentTime());
            logs.setPurposeOfVisit(visitor.getPurposeOfvisit());
            logsService.createVisitorLogs(getVisitor, logs);
            visitorsService.updateVisitor(getVisitor.getId(), getVisitor);
            session.setAttribute("visitor", getVisitor);
            //serialService.startListening(session);
            System.out.println("Welcome! Gate opened.");

            serialService.sendCommand("open");
    	}
    	System.out.println(getVisitor.getPurposeOfvisit()+getVisitor.getPurposeOfvisit());
    	 // Generate the success message and redirect
        String successMessage = String.format("%s %s \n%s %s\n%s",
        	getVisitor.getFirstname(),
        	getVisitor.getLastname(),
        	getVisitor.getEmail(),
        	getVisitor.getAddress(),

            dateTime.getCurrentTime());

        return "redirect:/smartgate?successMessage=" + successMessage + "&&imgUrl="+"/displayVisitor?id="+getVisitor.getId()+ "&&time=" + dateTime.getCurrentDay();
    }
    @PostMapping("/updateVisitor")
    @ResponseBody
    public String updateVisitor(@RequestParam("id") Long id,
                                 @RequestParam("firstname") String firstname,
                                 @RequestParam("lastname") String lastname,
                                 @RequestParam("email") String email,
                                 @RequestParam("address") String address,
                                 @RequestParam("registrationCode") String registrationCode,
                                 @RequestParam(value = "image", required = false) MultipartFile image) {
        try {	
            Visitors existingVisitor = visitorsService.getVisitorById(id);
            if (existingVisitor != null) {
                existingVisitor.setFirstname(firstname);
                existingVisitor.setLastname(lastname);
                existingVisitor.setEmail(email);
                existingVisitor.setAddress(address);
                existingVisitor.setRegistrationCode(registrationCode);
                if (image != null && !image.isEmpty()) {
                    byte[] bytes = image.getBytes();
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
                    existingVisitor.setImage(blob);
                }
                visitorsService.updateVisitor(id, existingVisitor);
                return "success";
            } else {
                return "error: visitor not found";
            }
        } catch (IOException e) {
            // Log the error and return a more informative message
            e.printStackTrace();
            return "error: file handling issue";
        } catch (SQLException e) {
            // Log the error and return a more informative message
            e.printStackTrace();
            return "error: database issue";
        } catch (Exception e) {
            // Log unexpected errors
            e.printStackTrace();
            return "error: unexpected issue";
        }
    }
    @PostMapping("/enableVisitor")
    public String enableVisitorMethod(Model model, @ModelAttribute Visitors visitor) {
    	Visitors getVisitor = visitorsService.getVisitorById(visitor.getId());
    	getVisitor.setStatus(1);
    	visitorsService.updateVisitor(visitor.getId(), getVisitor);
    	return "redirect:/getVisitors?successMessage=visitor enabled.";
    }
    @PostMapping("/disableVisitor")
    public String disableVisitorMethod(Model model, @ModelAttribute Visitors visitor) {
    	Visitors getVisitor = visitorsService.getVisitorById(visitor.getId());
    	getVisitor.setStatus(0);
    	visitorsService.updateVisitor(visitor.getId(), getVisitor);
    	return "redirect:/getVisitors?successMessage=visitor enabled.";
    }
    @PostMapping("/deleteVisitor")
    public String deleteVisitorMethod(Model model, @ModelAttribute Visitors visitor) {
    	visitorsRepository.deleteById(visitor.getId());
    	return "redirect:/getVisitors?successMessage=visitor deleted.";
    }
    @GetMapping("/generateQR")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam("text") String text) {
        try {
            byte[] qrCodeImage = QRCodeGenerator.generateQRCode(text, 200, 200);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return ResponseEntity.ok().headers(headers).body(qrCodeImage);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/registerGetCode")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateUser(@RequestParam("firstname") String firstname,
                             @RequestParam("lastname") String lastname,
                             @RequestParam("address") String address,
                             @RequestParam("email") String email,
                             @RequestParam(value = "image", required = false) MultipartFile image, Model model, HttpSession session) {
    	Visitors visitor = new Visitors();
    	Map<String, Object> response = new HashMap<>();
        Random random = new Random();
        int fiveDigitNumber = 10000 + random.nextInt(90000);
        DateTime dateTime = new DateTime();
        if (image != null && !image.isEmpty()) {
            byte[] bytes = null;
            try {
                bytes = image.getBytes();  // Get the image bytes
            } catch (IOException e) {
                e.printStackTrace();  // Handle any IO exceptions
            }
            
            if (bytes != null) {
                Blob blob = null;
                try {
                    blob = new javax.sql.rowset.serial.SerialBlob(bytes);  // Create a blob from the byte array
                } catch (SerialException e) {
                    e.printStackTrace();  // Handle any Serial exceptions
                } catch (SQLException e) {
                    e.printStackTrace();  // Handle SQL exceptions
                }
                
                // Set the blob in the visitor object if it's successfully created
                if (blob != null) {
                    visitor.setImage(blob);
                }
            }
        } else {
            System.out.println("Image file is either null or empty");  // Log when the image is null or empty
        }

        
        visitor.setLoggedIn(0);
        visitor.setFirstname(firstname);
        visitor.setLastname(lastname);
        visitor.setEmail(email);
        visitor.setAddress(address);
        visitor.setCreatedAt(dateTime.getCurrentDateTime());
        visitor.setRegistrationCode(String.valueOf(fiveDigitNumber));
        
        
        // Save visitor to the database
        visitorsService.createVisitor(visitor);
        model.addAttribute("confirmationCode", fiveDigitNumber);
        model.addAttribute("email", email);
        System.out.println(fiveDigitNumber+"dako manaaaaaa");
        response.put("confirmationCode", fiveDigitNumber);
        response.put("email", email);
        System.out.println(fiveDigitNumber + "dako manaaaaaa");

        return ResponseEntity.ok(response);


    }

}
