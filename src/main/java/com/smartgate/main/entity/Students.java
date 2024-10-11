package com.smartgate.main.entity;

import java.sql.Blob;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Students {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name="rfid_code")
	private String rfidCode;
	private String username;
	private String password;
	private String firstname;
	private String middlename;
	private String lastname;
	private String rfid;
	
	@Lob
	@Column(name="students_pic")
	private Blob studentsPic;
	public Blob getStudentsPic() {
		return studentsPic;
	}
	public void setStudentsPic(Blob studentsPic) {
		this.studentsPic = studentsPic;
	}
	@OneToMany(mappedBy = "students")
    private List<Logs> logs;
	@OneToMany(mappedBy = "students")
    private List<SecurityAlerts> SecurityAlerts;
	@Column(name="year_level")
	private String yearLevel;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRfidCode() {
		return rfidCode;
	}
	public void setRfidCode(String rfidCode) {
		this.rfidCode = rfidCode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getRfid() {
		return rfid;
	}
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	public String getYearLevel() {
		return yearLevel;
	}
	public void setYearLevel(String yearLevel) {
		this.yearLevel = yearLevel;
	}
	public String getProgramCode() {
		return programCode;
	}
	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getProgramDescription() {
		return programDescription;
	}
	public void setProgramDescription(String programDescription) {
		this.programDescription = programDescription;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPresent() {
		return present;
	}
	public void setPresent(String present) {
		this.present = present;
	}
	public Integer getLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(Integer i) {
		this.loggedIn = i;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getStudentsPicture() {
		return studentsPicture;
	}
	public void setStudentsPicture(String studentsPicture) {
		this.studentsPicture = studentsPicture;
	}
	@Column(name="program_code")
	private String programCode;
	private String section;
	@Column(name="program_description")
	private String programDescription;
	private String address;
	@Column(name="phone_number")
	private String phoneNumber;
	private String present;
	@Column(name="logged_in")
	private Integer loggedIn;
	@Column(name="created_at")
	private String createdAt;
	@Column(name="student's_picture")
	private String studentsPicture;
	
}
