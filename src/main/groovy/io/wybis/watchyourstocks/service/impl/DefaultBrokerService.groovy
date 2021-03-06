package io.wybis.watchyourstocks.service.impl

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.model.Role
import io.wybis.watchyourstocks.model.User
import io.wybis.watchyourstocks.model.constants.UserType
import io.wybis.watchyourstocks.service.AccountService
import io.wybis.watchyourstocks.service.BrokerService
import io.wybis.watchyourstocks.service.exceptions.ModelAlreadyExistException
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
@Slf4j
class DefaultBrokerService extends DefaultUserService implements BrokerService {

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

        if (!model.userId) {
            Branch branch = Branch.get(model.branchId)
            model.branch = branch
            model.userId = "${model.firstName}-${model.lastName}@${branch.code}"
            model.userId = model.userId.toLowerCase()
        }
        model.type = UserType.BROKER
        model.roleId = Role.ID_BROKER
        //model.branchId = sessionUser.branchId

        super.add(sessionUser, model)

        accountService.onBrokerCreate(sessionUser, model)
    }

    @Override
    public void onBranchCreate(SessionDto sessionUser, Branch branch) {

//		User model = new User()
//		model.with {
//			firstName = 'Guest'
//			lastName = 'Broker'
//			branchId = branch.id
//		}
//
//		this.add(sessionUser, model)
    }
}
