package com.myproject.hrmonitor.dto;

public class UserRegisterDto {
//    @NotBlank(message = "Логин обязателен")
//    @Size(min = 3, max = 20, message = "Логин должен быть от 3 до 20 символов")
    private String username;

//    @NotBlank(message = "Пароль обязателен")
//    @Size(min = 6, max = 20, message = "Логин должен быть от 6 до 20 символов")
    private String password;
    private String role;

    public UserRegisterDto() {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
