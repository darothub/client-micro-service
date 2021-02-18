package com.darothub.clientmicroservice.filter;

import com.darothub.clientmicroservice.entity.ErrorResponse;
import com.darothub.clientmicroservice.service.ClientService;
import com.darothub.clientmicroservice.utils.JWTUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private ClientService clientService;

    public static String  token = null;
    public static String userName = null;
    public static int userId = 0;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");


        try{
            if (null != authorization && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
                userName = jwtUtility.getEmailAddressFromToken(token);
                userId = jwtUtility.getIdFromToken(token);
            }
            else{
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString(), "You are not authorized");

                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getOutputStream(), error);
            }

        }
        catch (ExpiredJwtException e){
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString(), "JWT has expired");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), error);
        }
//
//
        if (0 != userId && null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("token "+token + "\n" + "userId " + userId);
//            UserDetails userDetails = clientService.loadUserByUsername(String.valueOf(userId));
//            UserDetails userDetails = clientService.getUserById(Long.valueOf(userId), token);
            UserDetails userDetails = clientService.getUserById(Long.valueOf(userId), token);
            log.info("UserDTO {}", userDetails);
            if (jwtUtility.validateTokenTwo(token)) {
                log.info("token is valid");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }



        filterChain.doFilter(request, response);
    }
}
