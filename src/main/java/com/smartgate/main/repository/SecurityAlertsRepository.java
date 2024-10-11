package com.smartgate.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smartgate.main.entity.Logs;
import com.smartgate.main.entity.SecurityAlerts;

public interface SecurityAlertsRepository extends JpaRepository<SecurityAlerts, Long> {
	List <SecurityAlerts> findByDateAndTime(String date,String time);
	List <SecurityAlerts> findByStudentsId(Long studentsId);
	List <SecurityAlerts> findByLogType(Long logType);
	@Query("SELECT s FROM SecurityAlerts s WHERE s.students IS NOT NULL")
	List<SecurityAlerts> findAllStudentSecurityAlerts();
	@Query("SELECT s FROM SecurityAlerts s WHERE s.students IS NULL")
	List<SecurityAlerts> findAllUnknown();
}
