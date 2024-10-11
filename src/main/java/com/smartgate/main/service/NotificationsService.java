package com.smartgate.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartgate.main.entity.Notifications;
import com.smartgate.main.entity.Students;
import com.smartgate.main.repository.NotificationsRepository;

@Service
public class NotificationsService {
	@Autowired
	private NotificationsRepository notificationsRepository;
	public Notifications updateNotifications(Long id, Notifications notifDetails) {
		Notifications notifications = notificationsRepository.findById(id).orElse(null);
	        if (notifications != null) {
	        	
	        	notifications.setHasNotif(notifDetails.getHasNotif());
	            return notificationsRepository.save(notifications);
	        }
	        return null;
	    }
}
