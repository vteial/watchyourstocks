package io.wybis.watchyourstocks.repository

import io.wybis.watchyourstocks.model.Product
import org.springframework.data.jpa.repository.JpaRepository

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTypeAndBranchId(String type, long branchId)

    Product findByCodeAndBranchId(String code, long branchId)
}
