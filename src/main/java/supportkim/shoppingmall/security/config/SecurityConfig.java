package supportkim.shoppingmall.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import supportkim.shoppingmall.api.member.MemberController;
import supportkim.shoppingmall.jwt.JwtAuthorizationFilter;
import supportkim.shoppingmall.jwt.JwtService;
import supportkim.shoppingmall.repository.MemberRepository;
import supportkim.shoppingmall.security.filter.CustomAuthenticationFilter;
import supportkim.shoppingmall.security.handler.CustomAuthenticationFailureHandler;
import supportkim.shoppingmall.security.handler.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MemberRepository memberRepository;



    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authRequest -> authRequest.requestMatchers(
                        new AntPathRequestMatcher("/**")
                        ).permitAll())
                // 앞에 있는게 뒤에 오는거 Before/After
                .addFilterBefore(authenticationFilter() , UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(myJwtFilter() , CustomAuthenticationFilter.class)
//                .exceptionHandling(config -> config.authenticationEntryPoint().accessDeniedHandler())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public CustomAuthenticationFilter authenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        return customAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
//        return new JwtAuthorizationFilter(authenticationManager(),jwtService , memberRepository);
//    }

    @Bean
    public JwtAuthorizationFilter myJwtFilter() {
        return new JwtAuthorizationFilter(jwtService , memberRepository);
    }

}
