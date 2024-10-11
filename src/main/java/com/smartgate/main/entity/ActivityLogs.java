package com.smartgate.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ActivityLogs")
public class ActivityLogs {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	private String startTimeStamp;
	@Column
	private String activity;
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getTimeOfActivity() {
		return timeOfActivity;
	}
	public void setTimeOfActivity(String timeOfActivity) {
		this.timeOfActivity = timeOfActivity;
	}
	@Column
	private String timeOfActivity;
	@Column
	private int isLogged;
	@Column
	private String endTimeStamp;
	public int getIsLogged() {
		return isLogged;
	}
	public void setIsLogged(int isLogged) {
		this.isLogged = isLogged;
	}
	@ManyToOne
    private UserEntity users;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStartTimeStamp() {
		return startTimeStamp;
	}
	public void setStartTimeStamp(String startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}
	public String getEndTimeStamp() {
		return endTimeStamp;
	}
	public void setEndTimeStamp(String endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}
	public UserEntity getUsers() {
		return users;
	}
	public void setUsers(UserEntity users) {
		this.users = users;
	}
	
	
}
