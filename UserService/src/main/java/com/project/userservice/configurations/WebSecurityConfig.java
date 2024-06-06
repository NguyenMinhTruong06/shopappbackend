package com.project.userservice.configurations;


import com.project.userservice.filter.JwtTokenFilter;
import com.project.userservice.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig implements WebMvcConfigurer {
    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)

//                .csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

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
                            .requestMatchers(POST,("/api/v1/orders/**")).hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(GET,("/api/v1/orders/**")).permitAll()
                            .requestMatchers(DELETE,("/api/v1/orders/**")).hasRole(Role.ADMIN)

                            .requestMatchers(PUT,("/api/v1/orderdetail")).hasRole(Role.ADMIN)
                            .requestMatchers(POST,("/api/v1/orderdetail/**")).permitAll()
                            .requestMatchers(GET,("/api/v1/orderdetail/**")).hasAnyRole(Role.ADMIN,Role.USER)
                            .requestMatchers(DELETE,("/api/v1/orderdetail/**")).hasRole(Role.ADMIN)

                            .requestMatchers(POST,("/api/v1/options/**")).hasAnyRole(Role.ADMIN)
                            .anyRequest().authenticated();
                });

        return http.build();

    }

}

