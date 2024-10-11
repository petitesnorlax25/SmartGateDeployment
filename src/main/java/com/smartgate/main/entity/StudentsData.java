package com.smartgate.main.entity;

import lombok.Data;

@Data
public class StudentsData {

	private String address;
    private String callback;
    private String cp_number;
    public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public String getCp_number() {
		return cp_number;
	}
	public void setCp_number(String cp_number) {
		this.cp_number = cp_number;
	}
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	public String getErr_msg() {
		return err_msg;
	}
	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	public boolean isIs_valid() {
		return is_valid;
	}
	public void setIs_valid(boolean is_valid) {
		this.is_valid = is_valid;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public boolean isLogin() {
		return login;
	}
	public void setLogin(boolean login) {
		this.login = login;
	}
	public String getMiddle_name() {
		return middle_name;
	}
	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}
	public String getProgram_code() {
		return program_code;
	}
	public void setProgram_code(String program_code) {
		this.program_code = program_code;
	}
	public String getProgram_description() {
		return program_description;
	}
	public void setProgram_description(String program_description) {
		this.program_description = program_description;
	}
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public String getRfid() {
		return rfid;
	}
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getUser_code() {
		return user_code;
	}
	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}
	public String getUser_group() {
		return user_group;
	}
	public void setUser_group(String user_group) {
		this.user_group = user_group;
	}
	public int getYear_level() {
		return year_level;
	}
	public void setYear_level(int year_level) {
		this.year_level = year_level;
	}
	private String email_address;
    private String err_msg;
    private String first_name;
    private String gender;
    private boolean is_active;
    private boolean is_valid;
    private String last_name;
    private boolean login;
    private String middle_name;
    private String program_code;
    private String program_description;
    private String request_id;
    private String rfid;
    private String section;
    private String user_code;
    private String user_group;
    private int year_level;
}
