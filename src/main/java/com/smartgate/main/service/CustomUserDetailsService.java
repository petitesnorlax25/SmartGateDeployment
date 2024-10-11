//package com.smartgate.main.service;
//
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import com.smartgate.main.repository.UserRepository;
//import com.smartgate.main.entity.UserEntity;
//import com.smartgate.main.entity.Role;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private UserRepository userRepository;
////    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
//	public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        List<UserEntity> users = userRepository.findByUsername(username);
//        if (users.isEmpty()) {
//            throw new UsernameNotFoundException("Username not found");
//        }
//        UserEntity user = users.get(0); // Assuming usernames are unique
//        return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
//    }
//
//    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> list) {
//        return list.stream()
//            .map(role -> new SimpleGrantedAuthority(role.getName()))
//            .collect(Collectors.toList());
//    }
//}
