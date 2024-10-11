package com.smartgate.main.entity;

import java.sql.Blob;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Visitors {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name="first_name")
	private String firstname;
	@Column(name="middle_name")
	private String middlename;
	@Column(name="last_name")
	private String lastname;
	@Column(name="purpose_of_visit")
	private String purposeOfvisit;
	@Column(name="created_at")
	private String createdAt;
	@Column
	private String address;
	@Column
	private String email;
	@Column
	private int status;
	@Column(name="logged_in")
	private Integer loggedIn;
	public int getStatus() {
		return status;
	}
	public Integer getLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(Integer loggedIn) {
		this.loggedIn = loggedIn;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public MultipartFile getImageFile() {
		return imageFile;
	}
	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getPurposeOfvisit() {
		return purposeOfvisit;
	}
	public void setPurposeOfvisit(String purposeOfvisit) {
		this.purposeOfvisit = purposeOfvisit;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Blob getImage() {
		return image;
	}
	public void setImage(Blob image) {
		this.image = image;
	}
	
	public String getRegistrationCode() {
		return registrationCode;
	}
	public void setRegistrationCode(String registrationCode) {
		this.registrationCode = registrationCode;
	}
	public String getNumberUsed() {
		return numberUsed;
	}
	public void setNumberUsed(String numberUsed) {
		this.numberUsed = numberUsed;
	}
	public List<Logs> getLogs() {
		return logs;
	}
	public void setLogs(List<Logs> logs) {
		this.logs = logs;
	}
	
	@Lob
	@Column 
	private Blob image;
	@Column(name="registration_code")
	private String registrationCode;
	@Column(name="number_used")
	private String numberUsed;
	@OneToMany(mappedBy = "visitors")
    private List<Logs> logs;
	@Transient
    private MultipartFile imageFile;
	@Lob
    @Column(name = "qr_code_image")
    private byte[] qrCodeImage;
	public byte[] getQrCodeImage() {
		return qrCodeImage;
	}
	public void setQrCodeImage(byte[] qrCodeImage) {
		this.qrCodeImage = qrCodeImage;
	}
	

}
