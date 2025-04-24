    package com.myproject.hrmonitor.security;

    import com.myproject.hrmonitor.service.UserService;
    import com.sun.net.httpserver.Authenticator;
    import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
    import org.springframework.web.filter.HiddenHttpMethodFilter;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {
        @Autowired
        private UserService userService;

        @Bean
        public static PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

        @Bean
        public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
            return new HiddenHttpMethodFilter();
        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/registration").anonymous()
                    .requestMatchers("/login").anonymous()
                    .requestMatchers("/hr/**").hasRole("HR")
                    .requestMatchers("/team-lead-hr/**").hasRole("TEAM_LEAD_HR")
                    .anyRequest().authenticated()

            ).formLogin(form -> form
                    .loginPage("/login")
                    .failureUrl("/login?error=true")
                    .permitAll()
                    .successHandler(authenticationSuccessHandler())
            ).logout(logout -> logout
                    .logoutUrl("/logout")
                    .permitAll()
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/login")
            ).csrf(AbstractHttpConfigurer::disable);

            return http.build();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
            auth.setUserDetailsService(userService);
            auth.setPasswordEncoder(passwordEncoder());
            return auth;
        }

        @Bean
        public AuthenticationSuccessHandler authenticationSuccessHandler(){
            return (request, response, authentication) -> {
                if(authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_TEAM_LEAD_HR"))){
                    response.sendRedirect("/team-lead-hr/dashboard");
                } else {
                    response.sendRedirect("hr/resume");
                }
            };
        }
    }
