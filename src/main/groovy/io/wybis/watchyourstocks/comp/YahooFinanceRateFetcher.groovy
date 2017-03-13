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

    @Resource
    RestTemplate restTemplate

    @Override
    ProductRate fetch() {
        ProductRate model = super.fetch()
        model.providedBy = 'in.finance.yahoo.com'

        String s = 'https://in.finance.yahoo.com/q?s=' + code.toUpperCase() + '&ql=0'
        ResponseEntity<String> res = restTemplate.getForEntity(s, String.class);
        if (res.statusCode == 200) {
            Document doc = Jsoup.parse(res.body)
            Element ele = doc.getElementById("yfs_l84_${code.toLowerCase()}");
            try {
                model.value = Double.parseDouble(ele.text())
            }
            catch (NumberFormatException nfe) {
                nfe.printStackTrace()
            }
        }
        model.fetchTime = new Date()

        return model
    }

}
