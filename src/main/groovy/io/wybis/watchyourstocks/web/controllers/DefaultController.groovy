package io.wybis.watchyourstocks.web.controllers

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

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

}
