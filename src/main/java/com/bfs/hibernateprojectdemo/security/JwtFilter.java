package com.bfs.hibernateprojectdemo.security;



import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bfs.hibernateprojectdemo.exception.InvalidTokenException;

//The jwt filter that we want to add to the chain of filters of Spring Security
@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	String requestURI = request.getRequestURI();

    
        if (requestURI.equals("/login") || requestURI.equals("/signup")) {
            filterChain.doFilter(request, response);
            return;
        }
    	try {
           
            Optional<AuthUserDetail> authUserDetail = jwtProvider.resolveToken(request);

         
            if (authUserDetail.isPresent()) {
                AuthUserDetail userDetail = authUserDetail.get();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // Continue with the filter chain
            filterChain.doFilter(request, response);

        } catch (InvalidTokenException ex) {
         
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"" + ex.getMessage() + "\",\"data\":null}"
            );
            response.getWriter().flush(); 
        }
    }


}
