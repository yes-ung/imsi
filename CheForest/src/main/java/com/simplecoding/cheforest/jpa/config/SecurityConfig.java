package com.simplecoding.cheforest.jpa.config;

import com.simplecoding.cheforest.jpa.auth.security.CustomOAuth2UserService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;   // ✅ 소셜 로그인 서비스
    private final CustomLoginSuccessHandler customLoginSuccessHandler; // ✅ 공용 성공 핸들러
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;  // ✅ 공용 로그아웃 핸들러

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // DispatcherType.FORWARD = jsp redirection 허용 , DispatcherType.INCLUDE = jsp:include 허용
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE).permitAll()
                        //  [1] 관리자 전용 페이지
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")

                        //  [2] 로그인 필요한 페이지
                        .requestMatchers(
                                "/mypage/**",
                                "/이런식으로 더 추가하면 됩니다./**"
                        ).authenticated()


                        // [3] 나머지 페이지는 모두 허용
                        .anyRequest().permitAll()
                )
                // ✅ 일반 로그인 설정
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .successHandler(customLoginSuccessHandler) // 성공 핸들러
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )
                // ✅ 소셜 로그인 설정
                .oauth2Login(oauth -> oauth
                        .loginPage("/auth/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(customLoginSuccessHandler) // 성공 핸들러
                )
                // ✅ 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                        .permitAll()

                )
                // 보안토큰설정(현재 비활성화, 나중에 추가해주세요! 설정하면 POST 부분은 전부 보안토큰걸림)
                .csrf(csrf -> csrf.disable()
                );

        return http.build();
    }
}
