package io.wybis.watchyourstocks.comp

import groovy.transform.Canonical
import io.wybis.watchyourstocks.model.ProductRate


@Canonical
abstract class RateFetcher {

    String code

    String exchange

    ProductRate fetch() {
        ProductRate model = new ProductRate()

        model.code = code
        model.exchange = exchange

        return model
    }

}
