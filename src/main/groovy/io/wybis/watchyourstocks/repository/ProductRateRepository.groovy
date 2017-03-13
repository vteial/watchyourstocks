package io.wybis.watchyourstocks.repository

import io.wybis.watchyourstocks.model.ProductRate
import org.springframework.data.jpa.repository.JpaRepository

public interface ProductRateRepository extends JpaRepository<ProductRate, Long> {

}
