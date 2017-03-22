package io.wybis.watchyourstocks.web.controllers

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.ResponseDto
import io.wybis.watchyourstocks.model.ProductRate
import io.wybis.watchyourstocks.util.Helper
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.RestTemplate

@Controller
@Slf4j
public class DefaultController extends AbstractController {

    @RequestMapping(value = '/ping', method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> ping() {
        Map<String, String> map = [:]

        map.put('ping', 'Ping Pong!!!')

//        log.debug("Debug map : {}", map)
//        log.info("Info map : {}", map)

        return map;
    }

    @RequestMapping(value = '/test-mail', method = RequestMethod.GET)
    public @ResponseBody
    ResponseDto testMail() {
        ResponseDto responseDto = new ResponseDto()
        try {
            SimpleMailMessage message = new SimpleMailMessage()
            message.with {
                from = 'no-reply@watchyourstocks.wybis.com'
                to = 'vteial@gmail.com'
                subject = 'watchyourstocks - test mail'
                text = 'This is test please ignore it.'
            }
            mailSender.send(message)
            responseDto.message = 'test mail successfully sent...'
        } catch (Throwable t) {
            t.printStackTrace()
            String s = "test mail failed..."
            responseDto.type = ResponseDto.ERROR
            responseDto.message = s
            responseDto.data = Helper.getStackTraceAsString(t)
        }

        return responseDto
    }

}
