package com.smartgate.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class SecurityAlerts {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@ManyToOne
    private Students students;
	@Column(name="log_type")
	private Long logType;
	@Column
	private String date;
	@Column
	private String time;
	@Column
	private String anomaly;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Students getStudents() {
		return students;
	}
	public void setStudents(Students students) {
		this.students = students;
	}
	public Long getLogType() {
		return logType;
	}
	public void setLogType(Long logType) {
		this.logType = logType;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAnomaly() {
		return anomaly;
	}
	public void setAnomaly(String anomaly) {
		this.anomaly = anomaly;
	}
	
	
}
