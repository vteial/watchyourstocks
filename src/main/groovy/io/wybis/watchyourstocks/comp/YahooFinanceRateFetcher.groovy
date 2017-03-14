package io.wybis.watchyourstocks.comp

import groovy.transform.Canonical
import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.model.ProductRate
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

import javax.annotation.Resource

@Component('yahoo')
@Slf4j
@Canonical
class YahooFinanceRateFetcher extends RateFetcher {

    static void main(def args) {
        println('started...')
        YahooFinanceRateFetcher rateFetcher = new YahooFinanceRateFetcher()
        rateFetcher.restTemplate = new RestTemplate()
        rateFetcher.code = 'BIOCON.NS'
        ProductRate rateInfo = rateFetcher.fetch()
        println('finished...')
    }

    @Resource
    RestTemplate restTemplate

    @Override
    ProductRate fetch() {
        ProductRate model = null
        try {
            model = super.fetch()
            model.providedBy = 'in.finance.yahoo.com'

            // https://in.finance.yahoo.com/quote/BIOCON.NS?ltr=1
            String s = 'https://in.finance.yahoo.com/quote/' + code.toUpperCase() + '?ltr=1'
            log.debug('url for the stock {} is {}', code, s)
            ResponseEntity<String> res = restTemplate.getForEntity(s, String.class);
            if (res.statusCode.value() == 200) {
                Document doc = Jsoup.parse(res.body)
                Element ele = doc.getElementsByAttributeValue('data-reactid', '247').first()
                try {
                    model.value = Double.parseDouble(ele.text().replace(',', ''))
                }
                catch (NumberFormatException nfe) {
                    log.error("rate fetching failed for stock ${code}", nfe)
                }
            } else {
                log.warn('rate fetching failed for the stock {} because it returns with status {}', code, res.statusCode.value())
            }
            model.fetchTime = new Date()
        } catch(Throwable t) {
            log.error("rate fetching failed for stock ${code}", t)
        }
        return model
    }

}
