package io.wybis.watchyourstocks.service.impl

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.comp.RateFetcher
import io.wybis.watchyourstocks.dto.RateMonitor
import io.wybis.watchyourstocks.model.ProductRate
import io.wybis.watchyourstocks.service.RateMonitorService
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.annotation.Resource

@Service
@Slf4j
class DefaultRateMonitorService extends AbstractService implements RateMonitorService {

    @Resource
    ClassPathResource jsonFirbaseCpr

    @Resource(name = 'yahoo')
    RateFetcher rateFetcher

    boolean flag

    @PostConstruct
    @Override
    public void init() {
        //log.debug('----------------------------------------------------------------------------')
        log.debug('rateMonitor initialization started...')

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(jsonFirbaseCpr.inputStream)
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
    boolean isMonitorRatesRunning() {
        return flag
    }

    @Scheduled(cron = "0 */5 9-16 * * MON-FRI")
    @Override
    void monitorRates() {
        log.info('rate monitor started')

        this.flag = true

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("rateMonitors");
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
                DefaultRateMonitorService.this.flag = false

                log.info('rate monitor finished')
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DefaultRateMonitorService.this.flag = false
                log.error('firebase error : {}', databaseError)
            }
        });
    }
}
