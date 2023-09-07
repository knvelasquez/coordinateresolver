package config.interceptor;

import config.exception.HeaderAuthorizationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HeaderAuthorizationHandlerInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION_HEADER_KEY = "AUTHORIZATION";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String AUTHORIZATION = request.getHeader(AUTHORIZATION_HEADER_KEY);
        if (AUTHORIZATION != null) {
            return true;
        }
        throw new HeaderAuthorizationNotFoundException();
    }
}
