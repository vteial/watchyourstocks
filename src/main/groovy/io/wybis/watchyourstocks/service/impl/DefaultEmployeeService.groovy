package io.wybis.watchyourstocks.service.impl

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.model.Role
import io.wybis.watchyourstocks.model.User
import io.wybis.watchyourstocks.model.constants.UserType
import io.wybis.watchyourstocks.repository.BranchRepository
import io.wybis.watchyourstocks.service.AccountService
import io.wybis.watchyourstocks.service.EmployeeService
import io.wybis.watchyourstocks.service.exceptions.ModelAlreadyExistException
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
@Slf4j
class DefaultEmployeeService extends DefaultUserService implements EmployeeService {

    @Resource
    BranchRepository branchRepository;

    @Resource
    AccountService accountService

    @Override
    public List<User> findByBranchId(long branchId) {
        List<User> models = null

        models = this.findByTypeAndBranchId(UserType.EMPLOYEE, branchId)

        return models
    }

    @Override
    public void add(SessionDto sessionUser, User model)
            throws ModelAlreadyExistException {

        model.type = UserType.EMPLOYEE
        //model.branchId = sessionUser.branchId

        super.add(sessionUser, model)

        accountService.onEmployeeCreate(sessionUser, model)
    }

    @Override
    public void onBranchCreate(SessionDto sessionUser, Branch branch) {
        User model = new User()

        model.userId = branch.id + '@' + branch.code
        model.with {
            password = '123'
            firstName = branch.name
            lastName = branch.id as String
            roleId = Role.ID_MANAGER
            branchId = branch.id
        }

        this.add(sessionUser, model)

        branch.virtualEmployeeId = model.id

        Date now = new Date()
        branch.preUpdate(sessionUser, now)

        this.branchRepository.save(branch)
    }
}
