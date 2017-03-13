package io.wybis.watchyourstocks.service

import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.model.Product
import io.wybis.watchyourstocks.service.exceptions.ModelAlreadyExistException;

interface ProductService {

    List<Product> findByTypeAndBranchId(String type, long branchId)

    Product findByCodeAndBranchId(String code, long branchId)

    void add(SessionDto sessionUser, Product model) throws ModelAlreadyExistException

    void onBranchCreate(SessionDto sessionUser, Branch product)
}
