package com.smartgate.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartgate.main.entity.Logs;
import com.smartgate.main.entity.Students;
import com.smartgate.main.entity.Visitors;
import com.smartgate.main.repository.LogsRepository;
@Service
public class LogsService {
	@Autowired
    private LogsRepository logsRepository;

    public List <Logs> getAllLogs() {
        return logsRepository.findAll();
    }

    public List<Logs> getLogsByStudentsId(Long id) {
        return logsRepository.findByStudentsId(id);
    }
    public List<Logs> getLogsByVisitorsId(Long id) {
        return logsRepository.findByVisitorsId(id);
    }
    public List<Logs> getLogsByProgramCode(String programCode) {
        return logsRepository.findLogsByProgramCode(programCode);
    }
    public List <Logs> getLogsByDateAndTime(String date, String time) {
        return logsRepository.findByDateAndTime(date, time);
    }
    public List <Logs> getLogsByLogType(Long logType) {
        return logsRepository.findByLogType(logType);
    }
    public Logs createLogs(Students student, Logs logs) {
        logs.setStudents(student);
    	return logsRepository.save(logs);
    }
    public Logs createVisitorLogs(Visitors visitor, Logs logs) {
        logs.setVisitors(visitor);
    	return logsRepository.save(logs);
    }
    public List<Logs> getLogsWithVisitors() {
        return logsRepository.findAllLogsWithVisitor(); // or findByVisitorsIsNotNull()
    }
    public List<Logs> getLogsWithStudents() {
        return logsRepository.findAllLogsWithStudent(); // or findByVisitorsIsNotNull()
    }

}
