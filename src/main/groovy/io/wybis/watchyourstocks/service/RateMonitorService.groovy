package io.wybis.watchyourstocks.service

interface RateMonitorService {

    void init();

    boolean isMonitorRatesRunning()

    void monitorRates()

}
