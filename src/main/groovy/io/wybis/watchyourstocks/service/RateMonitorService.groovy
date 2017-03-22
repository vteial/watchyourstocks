package io.wybis.watchyourstocks.service

interface RateMonitorService {

    static String MONITOR_STATUS_ON = 'on'

    static String MONITOR_STATUS_OFF = 'off'

    void init();

    boolean isRunning()

    String getMonitorStatus()

    void setMonitorStatus(String monitorStatus)

    void monitorRatesJob()

    void monitorRates()

}
