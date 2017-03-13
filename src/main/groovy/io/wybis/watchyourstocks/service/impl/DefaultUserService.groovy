package io.wybis.watchyourstocks.service.impl

import groovy.util.logging.Slf4j
import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.User
import io.wybis.watchyourstocks.model.constants.UserStatus
import io.wybis.watchyourstocks.repository.UserRepository
import io.wybis.watchyourstocks.service.UserService
import io.wybis.watchyourstocks.service.exceptions.ModelAlreadyExistException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Service('userService')
@Slf4j
public class DefaultUserService extends AbstractService implements UserService {

    @Resource
    UserRepository userRepository;

    @Override
    public List<User> findByTypeAndBranchId(String type, long branchId) {
        List<User> models = null

        models = this.userRepository.findByTypeAndBranchId(type, branchId)

        return models
    }

    @Override
    User findByUserId(String userId) {
        User model = null

        model = this.userRepository.findByUserId(userId)

        return model
    }

    @Transactional
    @Override
    public void add(SessionDto sessionUser, User model)
            throws ModelAlreadyExistException {
        model.userId = model.userId.toLowerCase()

        if (this.findByUserId(model.userId)) {
            throw new ModelAlreadyExistException()
        }

        if (model.password == null) {
            model.password = 'wybis1234'
        }
        model.token = UUID.randomUUID().toString()
        model.status = UserStatus.ACTIVE
        model.id = autoNumberService.nextNumber(sessionUser, User.ID_KEY)
        if (sessionUser.id == 0) {
            sessionUser.id = model.id
        }

        Date now = new Date()
        model.prePersist(sessionUser, now)

        model = this.userRepository.save(model)
    }
}
