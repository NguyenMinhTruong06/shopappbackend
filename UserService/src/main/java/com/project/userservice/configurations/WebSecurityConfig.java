package com.project.userservice.configurations;


import com.project.userservice.filter.JwtTokenFilter;
import com.project.userservice.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests->{
                    requests.requestMatchers(
                            ("/api/v1/users/login"),
                            ("/api/v1/users/register"),
                                    ("/api/v1/users/**")

                    )
                            .permitAll()
                            .requestMatchers(POST,("/api/v1/users/**")).permitAll()
                            .requestMatchers(GET,("/api/v1/categories/**")).permitAll()
                            .requestMatchers(POST,("/api/v1/categories/**")).hasAnyRole(Role.ADMIN)
                            .requestMatchers(PUT,("/api/v1/categories/**")).hasAnyRole(Role.ADMIN)
                            .requestMatchers(DELETE,("/api/v1/categories/**")).hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET,("/api/v1/products/**")).permitAll()
                            .requestMatchers(POST,("/api/v1/products/**")).hasAnyRole(Role.ADMIN)
                            .requestMatchers(PUT,("/api/v1/products/**")).hasAnyRole(Role.ADMIN)
                            .requestMatchers(DELETE,("/api/v1/products/**")).hasAnyRole(Role.ADMIN)



                            .requestMatchers(PUT,("/api/v1/orders")).hasRole(Role.ADMIN)
                            .requestMatchers(POST,("/api/v1/orders/**")).hasAnyRole(Role.USER)
                            .requestMatchers(GET,("/api/v1/orders/**")).hasAnyRole(Role.ADMIN,Role.USER)
                            .requestMatchers(DELETE,("/api/v1/orders/**")).hasRole(Role.ADMIN)

                            .requestMatchers(PUT,("/api/v1/orderdetail")).hasRole(Role.ADMIN)
                            .requestMatchers(POST,("/api/v1/orderdetail/**")).hasAnyRole(Role.USER)
                            .requestMatchers(GET,("/api/v1/orderdetail/**")).hasAnyRole(Role.ADMIN,Role.USER)
                            .requestMatchers(DELETE,("/api/v1/orderdetail/**")).hasRole(Role.ADMIN)
                            .anyRequest().authenticated();
                });

        return http.build();

    }
}

