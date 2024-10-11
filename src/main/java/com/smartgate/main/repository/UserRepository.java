package com.smartgate.main.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartgate.main.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByUsername(String username);
	List<UserEntity> findByPassword(String password);
	UserEntity findByUsernameAndPassword(String username,String password);
	List<UserEntity> findByStatus(int status);
	UserEntity findByProgramCode(String programCode);
	List<UserEntity> findByUserType(String userType);
	List<UserEntity> findByUserTypeNotAndUsernameNot(String usertype, String username);
	List<UserEntity> findByUserTypeNotInAndUsernameNot(String[] userTypes, String username);

//	boolean existedByUsername(String username);
}

