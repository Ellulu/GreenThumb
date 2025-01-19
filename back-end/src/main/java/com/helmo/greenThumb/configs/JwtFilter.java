package com.helmo.greenThumb.configs;

import java.io.IOException;
import java.net.InetAddress;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        System.out.println("IP detected: " + clientIp); // Log pour vérifier l'IP
        System.out.println("myIP" + InetAddress.getLocalHost().getHostAddress());
        String authHeader = request.getHeader("Authorization");
                System.out.println("Authorization header: " + authHeader);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    try {
                        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                        request.setAttribute("firebaseToken", decodedToken);
                    } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Token invalide");
                        return;
                    }
                }

                filterChain.doFilter(request, response);
    }
    
}
