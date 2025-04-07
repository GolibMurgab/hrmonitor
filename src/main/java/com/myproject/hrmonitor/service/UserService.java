package com.myproject.hrmonitor.service;

import com.myproject.hrmonitor.dto.UserRegisterDto;
import com.myproject.hrmonitor.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean save(UserRegisterDto userRegisterDto);
}
