package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtRequestFilter extends OncePerRequestFilter {
    @Value("${jwt.header.string}")
    public String HEADER_STRING;
    @Value("${jwt.token.prefix}")
    public String TOKEN_PREFIX;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX,"");
            try {
                username = jwtUtil.extractUsername(authToken);
            } catch (IllegalArgumentException e) {
                logger.error("An error occurred while fetching Username from Token", e);
//            } catch (ExpiredJwtException e) {
//                logger.warn("The token has expired", e);
//            } catch(SignatureException e){
//                logger.error("Authentication Failed. Username or Password not valid.");
            }
        } else {
            logger.warn("Couldn't find bearer string, header will be ignored");
        }

        if (!StringUtils.isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = userService.loadByName(username);

            if (jwtUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtUtil.getAuthenticationToken(
                        authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}

