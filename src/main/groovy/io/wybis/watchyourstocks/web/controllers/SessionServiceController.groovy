package io.wybis.watchyourstocks.web.controllers

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.ResponseDto
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.dto.UserDto
import io.wybis.watchyourstocks.model.AutoNumber
import io.wybis.watchyourstocks.service.AutoNumberService
import io.wybis.watchyourstocks.service.RateMonitorService
import io.wybis.watchyourstocks.service.SessionService
import io.wybis.watchyourstocks.service.exceptions.InvalidCredentialException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

import javax.annotation.Resource
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/sessions")
@Slf4j
public class SessionServiceController extends AbstractController {

    @Resource
    SessionService sessionService;

    @Resource
    AutoNumberService autoNumberService;

    @Resource
    RateMonitorService rateMonitorService;

    @RequestMapping(value = "/properties", method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto properties(HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        try {
            Map<String, Object> props = sessionService.properties(session);
            responseDto.setData(props);
            responseDto.setType(ResponseDto.SUCCESS);

        } catch (Throwable t) {
            String s = "Unable to retrieve session properties...";
            responseDto.setType(ResponseDto.ERROR);
            responseDto.setMessage(s);
        }

        return responseDto;
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public @ResponseBody
    ResponseDto signin(HttpSession session,
                      @RequestBody final UserDto userDto) {
        ResponseDto responseDto = new ResponseDto();

        try {
            if (userDto.userId) {
                userDto.userId = userDto.userId.toLowerCase()
            }

            sessionService.signIn(session, userDto);

            String loginResponseRedirectURI = session.getAttribute(SessionService.SESSION_LOGIN_REDIRECT_KEY)
            if (loginResponseRedirectURI) {
                loginResponseRedirectURI = loginResponseRedirectURI.substring(10)
                responseDto.data = loginResponseRedirectURI
            }

            responseDto.setType(ResponseDto.SUCCESS);
            responseDto.setMessage("Successfully signed in...");

        } catch (InvalidCredentialException t) {
            String s = "Invalid User Id or Password...";
            responseDto.setType(ResponseDto.ERROR);
            responseDto.setMessage(s);
        }

        return responseDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/sign-out", method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto signOut(HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        sessionService.signOut(session);

        responseDto.setType(ResponseDto.SUCCESS);
        responseDto.setMessage("Successfully signed out...");

        return responseDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = '/next-auto-number/{id}', method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto nextAutoNumber(@PathVariable('id') final String id, HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        SessionDto sessionUser = session.getAttribute(SessionService.SESSION_USER_KEY);

        long value = autoNumberService.nextNumber(sessionUser, id);
        AutoNumber an = new AutoNumber();
        an.setId(id);
        an.setValue(value);
        responseDto.setData(an);

//        log.debug("Debug AutoNumber {}", an);
//        log.info("Info  AutoNumber {}", an);

        return responseDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/rate-monitor-now", method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto rateMonitorNow(HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        if(rateMonitorService.isMonitorRatesRunning()) {
            responseDto.setType(ResponseDto.WARNING);
            responseDto.setMessage("Rate monitor already running...");

        } else {
            rateMonitorService.monitorRates();
            responseDto.setType(ResponseDto.SUCCESS);
            responseDto.setMessage("Successfully rate monitor started...");
        }

        return responseDto;
    }

}
