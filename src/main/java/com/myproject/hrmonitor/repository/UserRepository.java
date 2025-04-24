package com.myproject.hrmonitor.repository;

import com.myproject.hrmonitor.entity.Role;
import com.myproject.hrmonitor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByTeamLeadIsNullAndRoleNot(Role role);
    List<User> findAllByTeamLeadAndRole(User teamLead, Role role);
}
