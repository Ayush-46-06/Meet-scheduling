package com.meet.MeetSchedulling.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionCookieFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletResponse res = (HttpServletResponse) response;
        
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(res) {
            @Override
            public void addHeader(String name, String value) {
                if ("Set-Cookie".equalsIgnoreCase(name) && value.startsWith("JSESSIONID")) {
                    if (!value.toLowerCase().contains("samesite=none")) {
                        value += "; SameSite=None";
                    }
                    if (!value.toLowerCase().contains("secure")) {
                        value += "; Secure";
                    }
                }
                super.addHeader(name, value);
            }
            
            @Override
            public void setHeader(String name, String value) {
                if ("Set-Cookie".equalsIgnoreCase(name) && value.startsWith("JSESSIONID")) {
                    if (!value.toLowerCase().contains("samesite=none")) {
                        value += "; SameSite=None";
                    }
                    if (!value.toLowerCase().contains("secure")) {
                        value += "; Secure";
                    }
                }
                super.setHeader(name, value);
            }
        };
        
        chain.doFilter(request, wrapper);
    }
}
