package io.wybis.watchyourstocks.service.impl

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.model.constants.BranchStatus
import io.wybis.watchyourstocks.repository.BranchRepository
import io.wybis.watchyourstocks.service.*
import io.wybis.watchyourstocks.service.exceptions.ModelAlreadyExistException
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
@Slf4j
class DefaultBranchService extends AbstractService implements BranchService {

    @Resource
    BranchRepository branchRepository;

    @Resource
    ProductService productService

    @Resource
    EmployeeService employeeService

    @Resource
    BrokerService brokerService

    @Resource
    InvestorService investorService

    @Override
    public void add(SessionDto sessionUser, Branch model)
            throws ModelAlreadyExistException {

        model.id = autoNumberService.nextNumber(sessionUser, Branch.ID_KEY)
        model.status = BranchStatus.ACTIVE

        Date now = new Date()
        model.prePersist(sessionUser, now)

        model = this.branchRepository.save(model)

        productService.onBranchCreate(sessionUser, model)
        employeeService.onBranchCreate(sessionUser, model)
        brokerService.onBranchCreate(sessionUser, model)
        investorService.onBranchCreate(sessionUser, model)
    }
}
