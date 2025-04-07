package com.myproject.hrmonitor.entity;

import com.myproject.hrmonitor.entity.Role;
import jakarta.persistence.*;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

//    @ManyToMany
//    @JoinColumn(name = "teamlead", foreignKey = @ForeignKey(name = "users_id"))
    private Long teamLead;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(Long teamLead) {
        this.teamLead = teamLead;
    }
}

