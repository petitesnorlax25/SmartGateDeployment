package com.smartgate.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Notifications {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name="alert_type")
	private String alertType;
	@Column(name="has_notif")
	private Long hasNotif;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public Long getHasNotif() {
		return hasNotif;
	}
	public void setHasNotif(Long hasNotif) {
		this.hasNotif = hasNotif;
	}
	
}
