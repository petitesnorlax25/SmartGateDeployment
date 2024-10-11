package com.smartgate.main.service;



import com.smartgate.main.entity.UserEntity;

import com.smartgate.main.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public UserEntity getUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
    public UserEntity getUserByUsername(String username) {
        return (UserEntity) userRepository.findByUsername(username);
    }
    public List<UserEntity> getUserByPassword(String password) {
        return (List<UserEntity>) userRepository.findByPassword(password);
    }
    public List<UserEntity> getByUsertypeAndUsernameNot(String userType, String username) {
        return (List<UserEntity>) userRepository.findByUserTypeNotAndUsernameNot(userType, username);
    }
    public List<UserEntity> getUserByStatus(int status) {
        return (List<UserEntity>) userRepository.findByStatus(status);
    }
    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity updateUser(Long id, UserEntity userDetails) {
    	UserEntity admin = userRepository.findById(id).orElse(null);
        if (admin != null) {
        	admin.setFullname(userDetails.getFullname());
        	admin.setUsername(userDetails.getUsername());
        	admin.setPassword(userDetails.getPassword());
        	admin.setUserType(userDetails.getUserType());
        	admin.setContactNumber(userDetails.getContactNumber());
        	admin.setProgramCode(userDetails.getProgramCode());
        	admin.setGender(userDetails.getGender());
        	admin.setEmail(userDetails.getEmail());
            return userRepository.save(admin);
        }
        return null;
    }

    public void deleteUser(Long id) {
    	userRepository.deleteById(id);
    }

	public UserEntity updateUser(UserEntity existingAdmin) {
		return userRepository.save(existingAdmin);
		
	}

	public void deleteById(Long id) {
		
		userRepository.deleteById(id);

		// TODO Auto-generated method stub
		
	}


}
