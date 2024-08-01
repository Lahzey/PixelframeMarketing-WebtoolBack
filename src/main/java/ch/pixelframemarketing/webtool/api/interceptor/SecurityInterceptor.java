package ch.pixelframemarketing.webtool.api.interceptor;

import ch.pixelframemarketing.webtool.data.entity.User;
import ch.pixelframemarketing.webtool.logic.service.UserService;
import ch.pixelframemarketing.webtool.general.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!handleToken(request, response, handler)) return false;
        if (!handleSecureAnnotation(request, response, handler)) return false;
        
        return true;
    }

    private boolean handleToken(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (userService.getCurrentUser() != null) return true;
        
        final String requestTokenHeader = request.getHeader("Authorization");
        String userId = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.info("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.info("JWT Token has expired");
            }
        } else {
            logger.info("JWT Token does not begin with Bearer String");
        }

        if (userId != null) {
            userService.loginById(userId);
        }
        return true;
    }

    private boolean handleSecureAnnotation(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) return true;

        Method method = handlerMethod.getMethod();

        if (!method.isAnnotationPresent(Secure.class)) return true;
        
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be logged in to access this resource.");
            return false;
        }
        
        if (currentUser.getRole() == User.Role.ADMIN) return true; // Admin does not have to be explicitly allowed on every endpoint, it always passes
        
        User.Role[] allowedRoles = method.getAnnotation(Secure.class).roles();
        if (allowedRoles == null || allowedRoles.length == 0) return true;
        for (User.Role role : allowedRoles) {
            if (currentUser.getRole() == role) return true;
        }
        
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not allowed to access this resource.");
        return false;
    }
}