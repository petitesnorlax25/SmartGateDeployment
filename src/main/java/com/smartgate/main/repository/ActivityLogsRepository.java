package com.smartgate.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartgate.main.entity.ActivityLogs;
import com.smartgate.main.entity.Logs;

public interface ActivityLogsRepository extends JpaRepository<ActivityLogs, Long> {
	ActivityLogs findByStartTimeStamp(String startTime);
	ActivityLogs findByEndTimeStamp(String endTime);
	List<ActivityLogs> findByIsLoggedAndUsersUserType(int isLogged, String userType);
	List<ActivityLogs> findByUsersUserTypeAndEndTimeStampIsNotNull(String userType);
	List <ActivityLogs> findByUsersId(Long userId);
	List <ActivityLogs> findByUsersUserTypeNot(String userType);

}
