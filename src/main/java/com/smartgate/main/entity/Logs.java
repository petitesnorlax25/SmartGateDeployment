package com.smartgate.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
@Entity
public class Logs {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name="log_type")
	private Integer logType;
	@ManyToOne
    private Students students;
	@ManyToOne
    private Visitors visitors;
	public Students getStudents() {
		return students;
	}
	public void setStudents(Students students) {
		this.students = students;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getLogType() {
		return logType;
	}
	public void setLogType(Integer logType) {
		this.logType = logType;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public Visitors getVisitors() {
		return visitors;
	}
	public void setVisitors(Visitors visitors) {
		this.visitors = visitors;
	}
	public String getPurposeOfVisit() {
		return purposeOfVisit;
	}
	public void setPurposeOfVisit(String purposeOfVisit) {
		this.purposeOfVisit = purposeOfVisit;
	}
	public void setDate(String date) {
		this.date = date;
	}
	private String time;
	private String date;
	private String purposeOfVisit;

}
