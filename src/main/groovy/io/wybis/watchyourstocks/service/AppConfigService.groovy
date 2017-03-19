package io.wybis.watchyourstocks.service

import javax.servlet.ServletContext

interface AppConfigService {

    void init()

    String getConfigProperty(String key);

    void setConfigProperty(String key, String value);

}
