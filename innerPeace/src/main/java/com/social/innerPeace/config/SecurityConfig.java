package com.social.innerPeace.config;

import com.social.innerPeace.handler.CustomAuthenticationFailureHandler;
import com.social.innerPeace.provider.CustomAuthenticationProvider;
import com.social.innerPeace.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder encryptPassword() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Autowired
    @Lazy
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/css/**", "/js/**", "/user/account/**", "/user/**", "/board/**", "/comment/**").permitAll()
                                .requestMatchers("/admin/help/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/user/account/signin")
                        .usernameParameter("healer_id")
                        .passwordParameter("healer_pw")
                        .loginProcessingUrl("/user/account/signin")
                        .successHandler(new CustomAuthenticationSuccessHandler())
                        .failureHandler(new CustomAuthenticationFailureHandler()))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/account/signout"))
                        .logoutSuccessUrl("/board/post/list")
                        .invalidateHttpSession(true)
                        .permitAll());
        return http.build();
    }

}
