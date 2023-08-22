package com.example.demo.base.security;

import com.example.demo.base.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .securityMatcher("/api/**")
                .exceptionHandling(except -> except.authenticationEntryPoint(authenticationEntryPoint))
                .authorizeHttpRequests(request -> request
                        //로그인은 누구나 가능하게 POST만 허용
                        .requestMatchers(new AntPathRequestMatcher("/api/*/member/login","POST")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/*/articles","GET")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/*/articles/*","GET")).permitAll()
                        //그외의 경우에는 인증된 사용자만 가능하게 한다.
                        .anyRequest().authenticated())
                .cors(cors->cors.disable()) // 타도메인에서 APi 호출가능
                .csrf(csrf -> csrf.disable()) // CSRF 토큰 끄기
                .httpBasic(httpBasic -> httpBasic.disable()) //httpBasic 로그인 방법끄기
                .formLogin(formLogin -> formLogin.disable()) // 폼로그인 방식 끄기
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //UsernamePasswordAuthenticationFilter.class 이 필더가 작동하기전에
                // 엑세스 토큰으로 부터 로그인 처리
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        ;//세션을 쓰지 않겠다.



        return http.build();
    }


}
