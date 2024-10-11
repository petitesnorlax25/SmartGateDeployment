package com.smartgate.main.service;



import com.smartgate.main.entity.UserEntity;
import com.smartgate.main.entity.Visitors;
import com.smartgate.main.repository.UserRepository;
import com.smartgate.main.repository.VisitorsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VisitorsService {
    @Autowired
    private VisitorsRepository visitorRepository;

    public List<Visitors> getAllVisitors() {
        return visitorRepository.findAll();
    }

    public Visitors getVisitorById(Long id) {
        return visitorRepository.findById(id).orElse(null);
    }
    public Visitors getVisitorByRegistrationCode(String registrationCode) {
        return visitorRepository.findByRegistrationCode(registrationCode);
    }
    public Visitors getVisitorByEmail(String email) {
        return visitorRepository.findByEmail(email);
    }
    public Visitors createVisitor(Visitors visitor) {
        return visitorRepository.save(visitor);
    }
    public Optional<Visitors> getLatestVisitor() {
        return visitorRepository.findTopByOrderByIdDesc();
    }
    public Visitors updateVisitor(Long id, Visitors visitorDetails) {
    	Visitors visitor = visitorRepository.findById(id).orElse(null);
        if (visitor != null) {
        	visitor.setRegistrationCode(visitorDetails.getRegistrationCode());
        	visitor.setAddress(visitorDetails.getAddress());
        	visitor.setFirstname(visitorDetails.getFirstname());
        	visitor.setLastname(visitorDetails.getLastname());
        	visitor.setEmail(visitorDetails.getEmail());
        	visitor.setStatus(visitorDetails.getStatus());
            return visitorRepository.save(visitor);
        }
        return null;
    }
    public Map<String, Long> getVisitorCounts() {
        long currentVisitors = visitorRepository.countCurrentVisitors();
        long visitorsNotInCampus = visitorRepository.countVisitorsNotInCampus();
        
        Map<String, Long> visitorCounts = new LinkedHashMap<>();
        visitorCounts.put("currentVisitorsInCampus", currentVisitors);
        visitorCounts.put("visitorsNotInCampus", visitorsNotInCampus);
        
        return visitorCounts;
    }



}
