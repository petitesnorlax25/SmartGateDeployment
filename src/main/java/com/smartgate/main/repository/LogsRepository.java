package com.smartgate.main.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartgate.main.entity.Logs;


public interface LogsRepository extends JpaRepository<Logs, Long> {

//	@Query("SELECT l FROM Logs l JOIN FETCH l.students")
//    List<Logs> findAllWithStudents();
	List <Logs> findByDateAndTime(String date,String time);
	List <Logs> findByStudentsId(Long studentsId);
	List <Logs> findByVisitorsId(Long visitorsId);
	List <Logs> findByLogType(Long logType);
	@Query("SELECT l FROM Logs l JOIN FETCH l.students s WHERE s.programCode = :programCode")
	List<Logs> findLogsByProgramCode(@Param("programCode") String programCode);
	List<Logs> findByVisitorsIsNotNull();
	@Query("SELECT l FROM Logs l WHERE l.visitors IS NOT NULL")
	List<Logs> findAllLogsWithVisitor();
	@Query("SELECT l FROM Logs l WHERE l.students IS NOT NULL")
	List<Logs> findAllLogsWithStudent();
	@Query("SELECT s.programCode, COUNT(l) FROM Logs l JOIN l.students s GROUP BY s.programCode")
	List<Object[]> findTotalLogsByProgramCode();
	



}
