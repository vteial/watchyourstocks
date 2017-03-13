package io.wybis.watchyourstocks.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.service.*
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.annotation.Resource

@Service
@Slf4j
public class DefaultAppService extends AbstractService implements
        AppService {

    @Resource
    ClassPathResource jsonDefaultBranchCpr

    @Resource
    ObjectMapper jsonObjectMapper

    @Resource
    BranchService branchService

    @Resource
    ProductService productService

    @Resource
    EmployeeService employeeService

    @Resource
    BrokerService brokerService

    @Resource
    InvestorService investorService

    @PostConstruct
    @Override
    public void init() {
        log.debug('----------------------------------------------------------------------------')
        log.debug('app initialization started...')

        Branch model = jsonObjectMapper.readValue(jsonDefaultBranchCpr.inputStream, Branch.class)

        SessionDto sessionUser = new SessionDto()
        branchService.add(sessionUser, model)

        model.products.each { t ->
            t.branchId = model.id
            productService.add(sessionUser, t)
        }

        model.employees.each { t ->
            t.branchId = model.id
            t.userId = "${t.userId}@${model.code}"
            employeeService.add(sessionUser, t)
        }

        model.brokers.each { t ->
            t.branchId = model.id
            brokerService.add(sessionUser, t)
        }

        model.investors.each { t ->
            t.branchId = model.id
            investorService.add(sessionUser, t)
        }

        log.info('app initialized...')

        log.debug('app initialization finished...')
        log.debug('----------------------------------------------------------------------------')
    }

}
