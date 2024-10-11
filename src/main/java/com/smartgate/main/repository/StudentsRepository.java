package com.smartgate.main.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartgate.main.entity.Students;

public interface StudentsRepository extends JpaRepository<Students, Long> {


	Students findByUsernameAndPassword(String username,String password);
	Students findByRfid(String rfid);
	Students findByFirstnameAndLastname(String firstname, String lastname);
	List<Students> findByProgramCode(String programCode);
//	@Query("SELECT s FROM Students s JOIN s.logs l WHERE l.students_id = :studentsId")
//	List <Students> findStudentsId(@Param("studentsId") Long studentsId);
	@Query("SELECT COUNT(s) FROM Students s WHERE s.loggedIn = 1")
    long countLoggedInStudents();
	@Query("SELECT s.programCode, COUNT(s) " +
		       "FROM Students s WHERE s.loggedIn = 1 " +
		       "GROUP BY s.programCode")
	List<Object[]> countStudentsByProgramCode();
	@Query("SELECT s.yearLevel, COUNT(s) " +
		       "FROM Students s " +
		       "WHERE s.loggedIn = 1 AND s.programCode = :programCode " +
		       "GROUP BY s.yearLevel")
		List<Object[]> countByProgramCodeGroupByYearLevel(@Param("programCode") String programCode);



}
