package com.example.travel.config;

import com.example.travel.ext.JwtAuthenticationFilter;
import com.example.travel.ext.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    private static final String[] PUBLIC_URLS = { //이 URL은 권한 검사안함. 아래 내용은 임시임.
            "/signup", "/login", "/", "/logout"
    };

    private final JwtTokenProvider jwtTokenProvider;

    @Bean //회원 insert 서비스에서 비밀번호 암호화/복호화에 사용됨
    public PasswordEncoder passwordEncoderParser() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests()// 다음 리퀘스트에 대한 사용권한 체크
                .antMatchers(PUBLIC_URLS).permitAll() // 가입 및 인증 주소는 누구나 접근가능
                .and()
                .authorizeRequests()// 다음 리퀘스트에 대한 사용권한 체크
                .anyRequest().authenticated()// 그외 나머지 요청은 모두 인증된 회원만 접근 가능
                .and()
                .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리
                .httpBasic().disable() // 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .formLogin().loginPage("/login").permitAll()//로그인 기본 url 설정
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.
                .and()
                .logout() // 로그아웃 기능 작동함
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .addFilterBefore(
                        JwtAuthenticationFilter.of(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class).build();
        //인증을 처리하는 기본필터 UsernamePasswordAuthenticationFilter 대신 별도의 인증 로직을 가진 필터를 생성하고 사용하고 싶을 때 아래와 같이 필터를 등록하고 사용
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { //시큐리티 filter 제외, 그러나 OncePerRequestFilter는 시큐리티 필터가 아니라서 로직실행
        return (web) -> web.ignoring().antMatchers(PUBLIC_URLS);
    }
}
