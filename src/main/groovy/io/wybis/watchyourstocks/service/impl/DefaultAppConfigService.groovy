package io.wybis.watchyourstocks.service.impl

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.service.AppConfigService
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

@Service
@Slf4j
class DefaultAppConfigService extends AbstractService implements AppConfigService {

//    @PostConstruct
    @Override
    void init() {

    }

    @Override
    String getConfigProperty(String key) {
        return null
    }

    @Override
    void setConfigProperty(String key, String value) {

    }
}
