package xyz.melodyl.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.melodyl.pojo.User;
import xyz.melodyl.pojo.UserCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static xyz.melodyl.service.UserService.TOKEN_ATTRIBUTE_NAME;

public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserCache userCache;

    private final String[] legalMemberRegex =
            new String[] {"/test", "/user/authorize", "/table/context/.[^/\\s]*?","/table/data/.[^/\\s]*?", "/error/\\d\\d\\d"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(TOKEN_ATTRIBUTE_NAME);
        User user = (token != null) ? userCache.getUserIfExist(token) : null;

        if (user != null || isLegal(request.getServletPath())) {
            return true;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private boolean isLegal(String uri) {
        for (String record : legalMemberRegex) {
            if (uri.matches(record))
                return true;
        }

        return false;
    }
}
