package com.smartgate.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smartgate.main.entity.Students;
import com.smartgate.main.entity.Visitors;

public interface VisitorsRepository extends JpaRepository<Visitors, Long> {
	Visitors findByEmail(String email);
	Visitors findByAddress(String address);
	Visitors findByRegistrationCode(String registrationCode);
	Visitors findByFirstnameAndLastname(String firstname, String lastname);
	Optional<Visitors> findTopByOrderByIdDesc();
	@Query("SELECT COUNT(v) FROM Visitors v WHERE v.loggedIn = 1")
    long countLoggedInVisitors();
	 @Query("SELECT COUNT(v) FROM Visitors v WHERE v.loggedIn = 1")
	    long countCurrentVisitors();

	    @Query("SELECT COUNT(v) FROM Visitors v WHERE v.loggedIn = 0")
	    long countVisitorsNotInCampus();

}
