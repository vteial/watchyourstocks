package io.wybis.watchyourstocks.web.interceptors

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.service.SessionService
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Slf4j
class SecurityCheckInterceptor extends HandlerInterceptorAdapter {

    Map<String, Boolean> pubExactPaths = [:]

    List<String> pubRegexPaths = [], pvtRegexPaths = []

    @PostConstruct
    public void init() {
        pubExactPaths['/sessions/properties'] = true
        pubExactPaths['/sessions/sign-in'] = true
        pubExactPaths['/sessions/sign-out'] = true
        pubExactPaths['/sessions/sign-up'] = true
        pubExactPaths['/sessions/sign-up-confirm'] = true
        pubExactPaths['/sessions/reset-password-request'] = true
        pubExactPaths['/sessions/reset-password-confirm'] = true
        pubExactPaths['/sessions/reset-password'] = true

//      pubRegexPaths << '^/sessions/sign-up-confirm/\\d+/.*'

//      pvtRegexPaths << '^/sessions/events/event/\\d+$'

    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if (request.getRequestURI().contains(".")) {
            return super.preHandle(request, response, handler)
        }
        //System.out.println("0 - ${request.requestURI}")
        if (!request.requestURI.startsWith('/sessions/')) {
            return super.preHandle(request, response, handler)
        }
        //System.out.println("1 - ${request.requestURI}")
        if (pubExactPaths[request.requestURI] || this.isReqPathExistsIn(this.pubRegexPaths, request.requestURI)) {
            return super.preHandle(request, response, handler)
        }
        //System.out.println("2 - ${request.requestURI}")
        HttpSession session = request.session
        SessionDto sessionUser = (SessionDto) session.getAttribute(SessionService.SESSION_USER_KEY)
        if (sessionUser == null && this.isReqPathExistsIn(this.pvtRegexPaths, request.requestURI)) {
            session.setAttribute(SessionService.SESSION_LOGIN_REDIRECT_KEY, request.requestURI)
            response.sendRedirect('/index.html#/sign-in')
            return;
        }
        //System.out.println("3 - ${request.requestURI}")
        if (sessionUser != null && this.isReqPathExistsIn(this.pvtRegexPaths, request.requestURI)) {
            session.setAttribute(SessionService.SESSION_LOGIN_REDIRECT_KEY, request.requestURI)
            response.sendRedirect('/home.html#/' + request.requestURI.substring(10))
            return;
        }
        //System.out.println("4 - ${request.requestURI}")
        if (sessionUser == null) {
            response.sendError(419);
            return;
        }
        //System.out.println("4 - ${request.requestURI}")
        String arrivedUserId = request.getHeader('X-UserId')
        if (arrivedUserId != 'null' && sessionUser.userId != arrivedUserId) {
            response.sendError(419);
            return;
        }
        //System.out.println("5 - ${request.requestURI}")

        return super.preHandle(request, response, handler)
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

    private boolean isReqPathExistsIn(List<String> regexPaths, String reqPath) {
        boolean flag = false

        for (int i = 0; i < regexPaths.size(); i++) {
            String regexPath = regexPaths[i]
            flag = reqPath.matches(regexPath)
            if (flag) {
                i = regexPaths.size() + 1
            }
        }
        //System.out.println("${flag} - ${reqPath}")

        return flag;
    }

}
