package supportkim.shoppingmall.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import supportkim.shoppingmall.security.provider.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // 정적 자원 보안 필터 거치지 않음
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authRequest -> authRequest
                        // 상품 조회 , 상품 상세 조회 ,
                        .requestMatchers("/","/sign-up","/login","/kimchis","/kimchis/*","/cart").permitAll()
                        .requestMatchers("/mypage").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(formLogin-> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login_proc")
                        .defaultSuccessUrl("/")
                        .permitAll());
        return http.build();
    }
}
