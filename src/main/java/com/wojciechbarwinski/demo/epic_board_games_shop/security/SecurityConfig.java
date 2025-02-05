package com.wojciechbarwinski.demo.epic_board_games_shop.security;

import com.wojciechbarwinski.demo.epic_board_games_shop.security.components.JWTAuthEntryPoint;
import com.wojciechbarwinski.demo.epic_board_games_shop.security.components.JWTAuthenticationFilter;
import com.wojciechbarwinski.demo.epic_board_games_shop.security.components.JWTGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final HandlerExceptionResolver exceptionResolver;
    private final JWTAuthEntryPoint authEntryPoint;
    private final UserDetailsService userDetailsService;
    private final JWTGenerator tokenGenerator;


    public SecurityConfig(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver, UserDetailsService userDetailsService,
                          JWTAuthEntryPoint authEntryPoint, JWTGenerator tokenGenerator) {
        this.exceptionResolver = resolver;
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.tokenGenerator = tokenGenerator;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exc -> exc
                        .authenticationEntryPoint(authEntryPoint))
                .sessionManagement(ses -> ses
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/payU/**").permitAll()
                        .requestMatchers("/order/{hashId}/cancel").permitAll()
                        .requestMatchers("/order/{hashId}/payment").permitAll()
                        .requestMatchers("/order/shipment").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(tokenGenerator, userDetailsService, exceptionResolver);
    }
}
