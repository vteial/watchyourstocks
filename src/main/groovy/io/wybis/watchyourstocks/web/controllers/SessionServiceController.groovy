package io.wybis.watchyourstocks.web.controllers

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.ResponseDto
import io.wybis.watchyourstocks.dto.UserDto
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
    ResponseDto login(HttpSession session,
                      @RequestBody final UserDto userDto) {
        ResponseDto responseDto = new ResponseDto();

        try {
            sessionService.signIn(session, userDto);

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
    ResponseDto logout(HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        sessionService.signOut(session);

        responseDto.setType(ResponseDto.SUCCESS);
        responseDto.setMessage("Successfully signed out...");

        return responseDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/rate-monitor-now", method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto rateMonitorNow(HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        rateMonitorService.monitorRates();

        responseDto.setType(ResponseDto.SUCCESS);
        responseDto.setMessage("Successfully rate monitor started...");

        return responseDto;
    }

}
