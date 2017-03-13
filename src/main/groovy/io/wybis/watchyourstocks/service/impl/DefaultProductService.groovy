package io.wybis.watchyourstocks.service.impl

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.model.Product
import io.wybis.watchyourstocks.model.constants.ProductStatus
import io.wybis.watchyourstocks.model.constants.ProductType
import io.wybis.watchyourstocks.repository.ProductRepository
import io.wybis.watchyourstocks.service.AccountService
import io.wybis.watchyourstocks.service.ProductService
import io.wybis.watchyourstocks.service.exceptions.ModelAlreadyExistException
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
@Slf4j
class DefaultProductService extends AbstractService implements ProductService {

    @Resource
    ProductRepository productRepository;

    @Resource
    AccountService accountService

    @Override
    List<Product> findByTypeAndBranchId(String type, long branchId) {
        List<Product> models = null

        models = this.productRepository.findByTypeAndBranchId(type, branchId)

        return models
    }

    @Override
    public Product findByCodeAndBranchId(String code, long branchId) {
        Product model = null

        model = this.productRepository.findByCodeAndBranchId(code, branchId)

        return model
    }

    @Override
    public void add(SessionDto sessionUser, Product model)
            throws ModelAlreadyExistException {

        if (model.type == null) {
            model.type = ProductType.PRODUCT
        }
        model.status = ProductStatus.ACTIVE

        model.id = autoNumberService.nextNumber(sessionUser, Product.ID_KEY)

        Date now = new Date()
        model.prePersist(sessionUser, now)

        model = this.productRepository.save(model)

        accountService.onProductCreate(sessionUser, model)
    }

    @Override
    public void onBranchCreate(SessionDto sessionUser, Branch branch) {
        Product model = new Product()

        model.with {
            type = ProductType.CASH_CAPITAL
            code = 'CPT'
            name = 'CASH IN CAPITAL'
            baseUnit = 1
            denominator = 1
            buyRate = 1
            buyPercent = 1
            sellRate = 1
            sellPercent = 1
            handStockAverage = 1
            virtualStockAverage = 1
            branchId = branch.id
        }
        this.add(sessionUser, model)

        model = new Product()

        model.with {
            type = ProductType.CASH_EMPLOYEE
            code = 'CIE'
            name = 'CASH IN EMPLOYEE'
            baseUnit = 1
            denominator = 1
            buyRate = 1
            buyPercent = 1
            sellRate = 1
            sellPercent = 1
            handStockAverage = 1
            virtualStockAverage = 1
            branchId = branch.id
        }
        this.add(sessionUser, model)

        model.with {
            type = ProductType.PROFIT_EMPLOYEE
            code = 'PIE'
            name = 'PROFIT IN EMPLOYEE'
            baseUnit = 1
            denominator = 1
            buyRate = 1
            buyPercent = 1
            sellRate = 1
            sellPercent = 1
            handStockAverage = 1
            virtualStockAverage = 1
            branchId = branch.id
        }
        this.add(sessionUser, model)

        model.with {
            type = ProductType.CASH_BROKER
            code = 'CIB'
            name = 'CASH IN BROKER'
            baseUnit = 1
            denominator = 1
            buyRate = 1
            buyPercent = 1
            sellRate = 1
            sellPercent = 1
            handStockAverage = 1
            virtualStockAverage = 1
            branchId = branch.id
        }
        this.add(sessionUser, model)

        model.with {
            type = ProductType.CASH_INVESTOR
            code = 'CII'
            name = 'CASH IN INVESTOR'
            baseUnit = 1
            denominator = 1
            buyRate = 1
            buyPercent = 1
            sellRate = 1
            sellPercent = 1
            handStockAverage = 1
            virtualStockAverage = 1
            branchId = branch.id
        }
        this.add(sessionUser, model)
    }
}
