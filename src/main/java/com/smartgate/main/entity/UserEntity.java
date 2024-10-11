package com.smartgate.main.entity;

import java.sql.Blob;
import java.util.List;

import jakarta.persistence.JoinColumn;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;	
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fullname;
    
	@Column
    private String email;
    @Column(name="program_code")
    private String programCode;
	@Column
    private String gender;
    @Column(name="contact_number")
    private String contactNumber;
    @Column
    private String username;

	@Column
    private String password;
    
	@Column()
    private String role;
    
	@Column
    private int status;
    
	
	@Column(name="user_type")
    private String userType;
    
    @Column(name="created_at")
    private String createdAt;
    @Lob
    private Blob image;
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//        name = "user_roles",
//        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
//    )
    @OneToMany(mappedBy = "users")
    private List<ActivityLogs> activityLogs;
    public Long getId() {
        return id;
    }
    public String getProgramCode() {
		return programCode;
	}
	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}
	public void setId(Long id) {
        this.id = id;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
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
    public Blob getImage() {
        return image;
    }
    public void setImage(Blob image) {
        this.image = image;
    }
    public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}public String getGender() {
		return gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
