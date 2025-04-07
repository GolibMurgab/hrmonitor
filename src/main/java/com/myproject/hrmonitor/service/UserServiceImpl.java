package com.myproject.hrmonitor.service;

import com.myproject.hrmonitor.dto.UserRegisterDto;
import com.myproject.hrmonitor.entity.Role;
import com.myproject.hrmonitor.entity.User;
import com.myproject.hrmonitor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean save(UserRegisterDto userRegisterDto) {
        User user = new User();
        user.setUsername(userRegisterDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setRole(Role.valueOf(userRegisterDto.getRole()));
        if(userRepository.findByUsername(userRegisterDto.getUsername()).isEmpty()){
            userRepository.save(user);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userObject = this.userRepository.findByUsername(username);
        System.out.println("я тут");
        if(userObject.isEmpty()) throw new UsernameNotFoundException(username);
        return this.mapUserDetails(userObject.get());
    }

    public UserDetails mapUserDetails(User user){
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().getAuthority())
                .build();
    }
}
