package com.smartgate.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartgate.main.entity.Notifications;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
	Notifications getByAlertType(String alertType);
}
