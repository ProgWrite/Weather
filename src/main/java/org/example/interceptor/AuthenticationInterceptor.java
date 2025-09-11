package org.example.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserResponseDto;
import org.example.service.SessionService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String sessionId = getSessionIdFromCookies(request);
        Optional<UserResponseDto> user = sessionService.getUserBySession(sessionId);

        if (user.isPresent()) {
            request.setAttribute("user", user.get());
            request.setAttribute("isLoggedIn", true);
        } else {
            request.setAttribute("isLoggedIn", false);
            System.out.println("hello");
        }

        if (!user.isPresent() && sessionId != null) {
            sessionService.deleteIfSessionExpired(sessionId);
            Cookie cookie = new Cookie("sessionId", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        if (isAuthorizationRequired(request)) {
            if (user.isEmpty()) {
                sendUnauthorizedResponse(response, "Authentication required");
                return false;
            }
        }
        return true;
    }

    private String getSessionIdFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("sessionId".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private boolean isAuthorizationRequired(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/logout") || path.startsWith("/search");
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
        response.getWriter().flush();
    }

}