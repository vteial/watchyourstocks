package io.wybis.watchyourstocks.service.impl

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.comp.RateFetcher
import io.wybis.watchyourstocks.dto.RateMonitor
import io.wybis.watchyourstocks.model.ProductRate
import io.wybis.watchyourstocks.service.RateMonitorService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.annotation.Resource

@Service
@Slf4j
class DefaultRateMonitorService extends AbstractService implements RateMonitorService {

    @Resource
    ClassPathResource jsonFirebaseCpr

    @Resource(name = 'yahoo')
    RateFetcher rateFetcher

    boolean running

    @Value('${rateMonitorService.monitorStatus}')
    String monitorStatus;

    @Value('${rateMonitorService.cron}')
    String cronExpression;

    @PostConstruct
    @Override
    public void init() {
        //log.debug('----------------------------------------------------------------------------')
        log.debug('rateMonitor initialization started...')

        log.debug('rateMonitor status : {}', this.monitorStatus)
        if(!this.monitorStatus) {
            this.monitorStatus = MONITOR_STATUS_ON
        }
        log.debug('rateMonitor status : {}', this.monitorStatus)
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(jsonFirebaseCpr.inputStream)
                .setDatabaseUrl('https://stock-monster.firebaseio.com/')
                .build()
        FirebaseApp app = null
        try {
            app = FirebaseApp.initializeApp(options)
        }
        catch (Exception error) {
            log.info("firebase already exists...")
            app = FirebaseApp.getInstance()
        }
        log.info('Firebase App Name : {}', app.name)

        log.info('rateMonitor initialized...')

        log.debug('rateMonitor initialization finished...')
        //log.debug('----------------------------------------------------------------------------')
    }

    @Override
    boolean isRunning() {
        return running
    }

    @Override
    String getMonitorStatus() {
        return monitorStatus
    }

    @Override
    void setMonitorStatus(String monitorStatus) {
        this.monitorStatus = monitorStatus
    }

    @Scheduled(cron = '${rateMonitorService.cron}')
    @Override
    void monitorRatesJob() {
        try {
            if(this.monitorStatus == MONITOR_STATUS_OFF) {
                log.info('rate monitor skipped due to monitor status flag is in off state')
                log.info('rate monitor started')
            } else {
                this.monitorRates()
            }
        } catch (Throwable t) {
            this.running = false
            log.error('rate monitor job failed', t)
        }
    }

    @Override
    void monitorRates() {
        log.info('rate monitor started')

        this.running = true

        String rateMonitorTable = 'rateMonitors'
        if(env.activeProfiles.contains('dev')) {
            rateMonitorTable = 'rateMonitorsDev'
        }

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(rateMonitorTable);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long size = dataSnapshot.getChildrenCount()
                log.info("Total stocks to monitor rates is {} :", size)
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                while (items.hasNext()) {
                    DataSnapshot item = (DataSnapshot) items.next();
                    RateMonitor rateMonitor = item.getValue(RateMonitor.class)
                    log.info("fetching rate for stock {}", rateMonitor.code)

                    rateFetcher.code = rateMonitor.code
                    ProductRate rateInfo = rateFetcher.fetch()
                    if (!rateInfo) {
                        continue
                    }
                    Map<String, Object> values = [:]
                    if (rateInfo.value < rateMonitor.lowerValue || rateInfo.value > rateMonitor.upperValue) {
                        values['status'] = 'yes'
                        values['metTime'] = rateInfo.fetchTime.time
                    } else {
                        values['status'] = 'no'
                    }
                    values['value'] = rateInfo.value

                    values['updateTime'] = rateInfo.fetchTime.time

                    DatabaseReference tref = item.getRef()
                    tref.updateChildren(values)
                    log.debug('RateMonitor : {}', values)
                }
                DefaultRateMonitorService.this.running = false

                log.info('rate monitor finished')
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DefaultRateMonitorService.this.running = false
                log.error('firebase error : {}', databaseError)
                log.info('rate monitor finished')
            }
        });
    }
}
