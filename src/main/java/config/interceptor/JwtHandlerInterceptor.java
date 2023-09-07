package config.interceptor;

import application.service.JwtService;
import config.exception.JwtNotValidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.SecurityKey;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtHandlerInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION_HEADER_KEY = "AUTHORIZATION";
    private final JwtService jwtService;

    public JwtHandlerInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String jwt = request.getHeader(AUTHORIZATION_HEADER_KEY).replace("Bearer ", "");
        SecurityKey securityKey = jwtService.decode(jwt);
        if (securityKey != null) {
            return true;
        }
        throw new JwtNotValidException();
    }
}
