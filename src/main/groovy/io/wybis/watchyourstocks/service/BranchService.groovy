package io.wybis.watchyourstocks.service;

import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.service.exceptions.ModelAlreadyExistException

interface BranchService {

    void add(SessionDto sessionUser, Branch branch) throws ModelAlreadyExistException
}
