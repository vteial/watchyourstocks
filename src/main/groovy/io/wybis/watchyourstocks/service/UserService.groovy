package io.wybis.watchyourstocks.service

import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.User
import io.wybis.watchyourstocks.service.exceptions.ModelAlreadyExistException

public interface UserService {

    List<User> findByTypeAndBranchId(String type, long branchId)

    User findByUserId(String userId)

    void add(SessionDto sessionDto, User model) throws ModelAlreadyExistException;
}
