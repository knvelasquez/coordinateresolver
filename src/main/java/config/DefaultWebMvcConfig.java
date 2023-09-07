package config;

import config.interceptor.HeaderAuthorizationHandlerInterceptor;
import config.interceptor.JwtHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DefaultWebMvcConfig implements WebMvcConfigurer {
    private final HeaderAuthorizationHandlerInterceptor headerAuthorizationHandlerInterceptor;
    private final JwtHandlerInterceptor jwtHandlerInterceptor;

    public DefaultWebMvcConfig(HeaderAuthorizationHandlerInterceptor headerAuthorizationHandlerInterceptor, JwtHandlerInterceptor jwtHandlerInterceptor) {
        this.headerAuthorizationHandlerInterceptor = headerAuthorizationHandlerInterceptor;
        this.jwtHandlerInterceptor = jwtHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerAuthorizationHandlerInterceptor);
        registry.addInterceptor(jwtHandlerInterceptor);
    }
}
