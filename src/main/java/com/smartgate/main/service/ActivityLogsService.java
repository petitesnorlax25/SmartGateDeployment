package com.smartgate.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartgate.main.entity.ActivityLogs;
import com.smartgate.main.entity.UserEntity;
import com.smartgate.main.repository.ActivityLogsRepository;

@Service
public class ActivityLogsService {
	@Autowired
	private ActivityLogsRepository activityLogsRepository;
	public List<ActivityLogs> getAllActivityLogs(){
		return activityLogsRepository.findAll();
	}
	public ActivityLogs getActivityLogsByUsersId(Long id){
		return activityLogsRepository.findById(id).orElse(null);
	}
	
	public ActivityLogs getActivityLogsByStartTimeStamp(String startTime){
		return activityLogsRepository.findByStartTimeStamp(startTime);
	}
	public ActivityLogs createActivityLogs(UserEntity users, ActivityLogs logs){
		logs.setUsers(users);
		return activityLogsRepository.save(logs);
	}
	public List<ActivityLogs> getByIsLoggedAndUserType(int isLogged, String userType){
		return activityLogsRepository.findByIsLoggedAndUsersUserType(isLogged, userType);
	}
	public List<ActivityLogs> getByUserUserType(String userType){
		return activityLogsRepository.findByUsersUserTypeAndEndTimeStampIsNotNull(userType);
	}

	public List<ActivityLogs> getByUserTypeNot( String userType){
		return activityLogsRepository.findByUsersUserTypeNot( userType);
	}
	public ActivityLogs updateActivityLogs(Long id, ActivityLogs logs){
		ActivityLogs activityLogs = activityLogsRepository.findById(id).orElse(null);
		if (activityLogs!=null) {
			activityLogs.setEndTimeStamp(logs.getEndTimeStamp());
			activityLogs.setIsLogged(logs.getIsLogged());
			return activityLogsRepository.save(activityLogs);
		}
		
		return null;
	}
}
