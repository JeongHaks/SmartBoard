package com.example.smartboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/* @Configuration
* 이 클래스가 설정도(설계도)임을 스프링에게 알리기 위한 어노테이션
* 이 클래스가 설정 클래스임을 스프링에게 알린다.
* */
@Configuration
@EnableWebSecurity // Spring Security 활성화하는 어노테이션 (웹 보안 필터 체인 등록)
public class SecurityConfig {
    /*
    * 보안 규칙의 '핵심' 을 담는 Bean
    *  - 어떤 URL은 누구나 접근이 가능하다. (permitAll)
    *  - 어떤 URL은 로그인이 필요로 하다. (authenticated)
    *  - CSRF : 폼 로그인 등 정책을 설정하는 함수.
    * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // REST API 개발 초기에는 CSRF를 끄는 경우가 많다.
                // 이유는 : CSRF는 브라우저 세션/쿠키 기반 보호장치라서이다.
                // 세션-폼로그인을 안 쓰는 REST API에선 불필요 및 번거롭다

                // 초기 REST API 개발 편의상 CSRF 비활성화(disable())한다.
                .csrf(csrf -> csrf.disable())
                // 요청(Endpoint)별 접근 권한 규칙을 정한다.
                .authorizeHttpRequests(auth -> auth
                // 1) 회원가입 및 로그인은 누구나 접근 가능하게끔 인증없이 접근 허용 (권한 부여 (인증이 불필요하다.))
                .requestMatchers("/sb/signup","/sb/login","/start").permitAll()
                // 2) 정적 리소스도 열어두면 편하다(필수가 아닌 선택)
                .requestMatchers(HttpMethod.GET, "/","/css/**","/js/**","/images/**","/favicon.ico").permitAll()
                // 3) 그 외 모든 요청은 인증이 필요하다.
                        .anyRequest().authenticated()
                )
                // 기본 HTML 로그인 폼은 사용하지 않는다(REST API 스타일)
                .formLogin(form -> form.disable())

                // JWT 등 토큰 방식으로 갈 때는 아래 주석 해제하여 사용하면 된다.
                // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // CORS 가 필요하면 여기서 주석해제 후 활성화 (프론트 분리 시 유용하다)
                // .cors(Customizer.withDefaults())
                ;
        return http.build();
    }
    /*
    *  비밀번호 암호화기(BCrypt) 등록
    *  - 회원가입 할 때 : 평문 비밀번호를 해시(암호문)으로 변환해서 저장
    *  - 로그인 할 때 : 입력 평문과 저장 해시를 비교(매칭)하는데 사용!!
    * */

    // 회원가입시 비밀번호 암호화하기 위한 함수
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
