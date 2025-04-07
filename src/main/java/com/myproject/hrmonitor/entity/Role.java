package com.myproject.hrmonitor.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    HR,
    TEAM_LEAD_HR;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
