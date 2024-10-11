package com.smartgate.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartgate.main.entity.Logs;
import com.smartgate.main.entity.SecurityAlerts;
import com.smartgate.main.entity.Students;
import com.smartgate.main.entity.Visitors;
import com.smartgate.main.repository.SecurityAlertsRepository;
@Service
public class SecurityAlertsService {
	@Autowired
    private SecurityAlertsRepository securityAlertsRepository;

    public List <SecurityAlerts> getAllLogs() {
        return securityAlertsRepository.findAll();
    }
    public List<SecurityAlerts> getLogsByStudentsId(Long id) {
        return securityAlertsRepository.findByStudentsId(id);
    }
    public List<SecurityAlerts> getLogsByLogType(Long logType) {
        return securityAlertsRepository.findByLogType(logType);
    }
    public List<SecurityAlerts> getLogsByDateAndTime(String date, String time) {
        return securityAlertsRepository.findByDateAndTime(date, time);
    }
    public List<SecurityAlerts> getStudentSecurityAlerts() {
        return securityAlertsRepository.findAllStudentSecurityAlerts();
    }
    public List<SecurityAlerts> getUnknownSecurityAlerts() {
        return securityAlertsRepository.findAllUnknown();
    }
    public SecurityAlerts createSecurityAlerts(Students students, SecurityAlerts securityAlerts) {
    	securityAlerts.setStudents(students);
    	return securityAlertsRepository.save(securityAlerts);
    }
}
