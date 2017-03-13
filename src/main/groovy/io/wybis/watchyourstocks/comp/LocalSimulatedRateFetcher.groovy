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

@Component('local')
@Slf4j
@Canonical
class LocalSimulatedRateFetcher extends RateFetcher {

    @Resource
    RestTemplate restTemplate

    double lowerValue

    double upperValue

    String domainPrefix

    @Override
    ProductRate fetch() {
        ProductRate model = super.fetch()
        model.exchange = exchange
        model.providedBy = 'stock-monster.appsopt.com'

        String s = "${domainPrefix}/rate-simulator?symbol=" + code.toUpperCase()
        s += "&lowerValue=${lowerValue}&upperValue=${upperValue}"
        ResponseEntity<String> res = restTemplate.getForEntity(s, String.class);
        if (res.statusCode == 200) {
            Document doc = Jsoup.parse(res.body)
            Element ele = doc.getElementById("yfs_l84_${code}");
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
