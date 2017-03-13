package io.wybis.watchyourstocks.service

import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.model.User
import io.wybis.watchyourstocks.service.exceptions.ModelAlreadyExistException;

interface InvestorService {

    List<User> findByBranchId(long branchId)

    void add(SessionDto sessionUser, User investor) throws ModelAlreadyExistException

    void onBranchCreate(SessionDto sessionUser, Branch branch)
}
