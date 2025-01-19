package com.helmo.greenThumb;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class IpRestrictionFilter implements Filter {

    private static final Set<String> allowedIps = new HashSet<>();

    static {
        // Ajouter ici les IP autorisées
        allowedIps.add("51.195.62.118");
        allowedIps.add("127.0.0.1");
        allowedIps.add("172.17.0.1");
        allowedIps.add("0:0:0:0:0:0:0:1");

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String remoteIp = httpServletRequest.getRemoteAddr();

        if (allowedIps.contains(remoteIp)) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.getWriter().write("Forbidden: IP address not allowed");
        }
    }

    @Override
    public void destroy() {
    }
}
