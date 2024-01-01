package com.hw.email.filter;

import com.auth0.jwt.interfaces.Claim;
import com.hw.email.auth.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Map;

@WebFilter(urlPatterns = {"/email/*", "/draft/*"})
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        response.setCharacterEncoding("UTF-8");

        final String tokenHeader = request.getHeader("authorization");

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("No token found");
            return;
        }

        String[] tokenArr = tokenHeader.split(" ");
        if (tokenArr.length != 2) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("No token found");
            return;
        }
        String token = tokenArr[1];
        Map<String, Claim> userData = JwtUtil.verifyToken(token);
        if (userData == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid token");
            return;
        }
        String userId = userData.get("userId").asString();
        request.setAttribute("userId", userId);
        chain.doFilter(req, res);
    }

}
