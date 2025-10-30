package com.example.logi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 인메모리 유저 (아이디/비번: admin/1234)
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("admin")
                .password("{noop}1234")   // {noop} = 암호화 안함
                .roles("ADMIN")
                .build()
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/admin/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")                          // 커스텀 로그인 페이지
                .loginProcessingUrl("/login")                 // 로그인 POST 처리 URL
                .defaultSuccessUrl("/admin/dashboard", true)  // 로그인 성공 후 이동
                .failureUrl("/login?error")                   // 실패 시
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            // CSRF는 유지. 폼에 hidden으로 토큰만 넣어주면 됨.
            .csrf(Customizer.withDefaults());

        return http.build();
    }
}