package ua.darksoul.testprojects.ownchat.util;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null) {
            String url = request.getRequestURI();
            if(!StringUtils.isEmpty(request.getQueryString())) {
                url += "?" + request.getQueryString();
            }

            response.setHeader("Turbolinks-Location", url);
        }
    }
}
