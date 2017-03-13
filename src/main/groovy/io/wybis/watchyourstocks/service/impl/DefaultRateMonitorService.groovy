package io.wybis.watchyourstocks.service.impl

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.comp.RateFetcher
import io.wybis.watchyourstocks.comp.YahooFinanceRateFetcher
import io.wybis.watchyourstocks.dto.RateMonitor
import io.wybis.watchyourstocks.model.ProductRate
import io.wybis.watchyourstocks.service.RateMonitorService
import org.springframework.core.io.ClassPathResource
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

    @PostConstruct
    @Override
    public void init() {
        log.debug('----------------------------------------------------------------------------')
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
            System.out.println("firebase already exists...")
            app = FirebaseApp.getInstance()
        }
        log.info('Firebase App Name : {}', app.name)

        log.info('rateMonitor initialized...')

        log.debug('rateMonitor initialization finished...')
        log.debug('----------------------------------------------------------------------------')
    }

    //@Scheduled(cron = "0 59 23 * * ?")
    @Override
    void monitorRates() {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("rateMonitors");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long size = dataSnapshot.getChildrenCount()
                System.out.println("Size : ${size}")
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                while (items.hasNext()) {
                    DataSnapshot item = (DataSnapshot) items.next();
                    RateMonitor ratePolicy = item.getValue(RateMonitor.class)
                    System.out.println(ratePolicy)

                    rateFetcher.code = ratePolicy.code
                    ProductRate rateInfo = rateFetcher.fetch()

                    Map<String, Object> values = [:]
                    if (rateInfo.value < ratePolicy.lowerValue || rateInfo.value > ratePolicy.upperValue) {
                        values['status'] = 'yes'
                        values['metTime'] = rateInfo.fetchTime.time
                    } else {
                        values['status'] = 'no'
                    }
                    values['value'] = rateInfo.value

                    values['updateTime'] = rateInfo.fetchTime.time

                    DatabaseReference tref = item.getRef()
                    tref.updateChildren(values)
                    System.out.println(values)
                    System.out.println('---------------------------------------------------------------')
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        });

    }
}
