package com.smartgate.main.service;

import com.smartgate.main.entity.Students;
import com.smartgate.main.repository.StudentsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentsService {
    @Autowired
    private StudentsRepository studentsRepository;

    public List<Students> getAllStudents() {
        return studentsRepository.findAll();
    }
    public List<Students> getStudentsByProgramCode(String programCode) {
        return studentsRepository.findByProgramCode(programCode);
    }

    public Students getStudentById(Long id) {
        return studentsRepository.findById(id).orElse(null);
    }
    public Students getStudentByUsernameAndPassword(String username, String password) {
        return studentsRepository.findByUsernameAndPassword(username, password);
    }
    public Students getStudentByFirstnameAndLastname(String firstname, String lastname) {
        return studentsRepository.findByFirstnameAndLastname(firstname, lastname);
    }
    public Students getStudentsByRfid(String rfid) {
        return studentsRepository.findByRfid(rfid);
    }
    public Students createStudents(Students students) {
        return studentsRepository.save(students);
    }
    public Students updateStudent(Long id, Students studentDetails) {
    	Students student = studentsRepository.findById(id).orElse(null);
        if (student != null) {
        	student.setFirstname(studentDetails.getFirstname());
        	student.setLastname(studentDetails.getLastname());
        	student.setMiddlename(studentDetails.getMiddlename());
        	student.setProgramCode(studentDetails.getProgramCode());
        	student.setSection(studentDetails.getSection());
        	student.setYearLevel(studentDetails.getYearLevel());
        	student.setProgramDescription(studentDetails.getProgramDescription());
        	student.setCreatedAt(studentDetails.getCreatedAt());
        	student.setAddress(studentDetails.getAddress());
        	student.setLoggedIn(studentDetails.getLoggedIn());
            return studentsRepository.save(student);
        }
        return null;
    }
    public Students updateLoggedType(Long id, Integer loggedIn) {
    	Students student = studentsRepository.findById(id).orElse(null);
    	student.setLoggedIn(loggedIn);
    	return studentsRepository.save(student);
    	
    }
}

