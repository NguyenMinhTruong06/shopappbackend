package com.project.userservice.filter;


import com.project.userservice.component.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request
            ,@NonNull HttpServletResponse response
            ,@NonNull FilterChain filterChain) throws ServletException, IOException {
//        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        try{
            if(isBypassToken(request)){
                filterChain.doFilter(request,response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            if (authHeader!=null && authHeader.startsWith("Bearer ")){
                final String token = authHeader.substring(7);
                final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
                if( phoneNumber != null && SecurityContextHolder.getContext().getAuthentication()==null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
                    if(jwtTokenUtil.validateToken(token, userDetails)){
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
            filterChain.doFilter(request,response);
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"unauthorized");

        }


    }
    private boolean isBypassToken (@NonNull HttpServletRequest request){
        final List<Pair<String,String>> bypassTokens = Arrays.asList(
                Pair.of("api/v1/products/**","GET"),
                Pair.of("api/v1/categories/**","GET"),
                Pair.of("api/v1/users/register","POST"),
                Pair.of("api/v1/users/login","POST")


                );

        for(Pair<String,String>bypassToken:bypassTokens){
            if(request.getServletPath().contains(bypassToken.getKey())&&
                    request.getMethod().equals(bypassToken.getValue())){
               return true;
            }
        }
        return false;
    }
}
