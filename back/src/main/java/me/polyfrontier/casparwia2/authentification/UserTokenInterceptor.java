package me.polyfrontier.casparwia2.authentification;

import me.polyfrontier.casparwia2.model.UserEntity;
import me.polyfrontier.casparwia2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class UserTokenInterceptor implements HandlerInterceptor {
    private final UserEntity currentUser;

    @Autowired
    private UserRepository userRepository;

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public UserTokenInterceptor(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            Optional<UserEntity> User = userRepository.findByToken(authorizationHeaderValue.substring(7));
            User.ifPresent(currentUser::populate);
        }else {
            currentUser.setGuest();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

}