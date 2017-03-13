package io.wybis.watchyourstocks.service

import io.wybis.watchyourstocks.dto.SessionDto
import io.wybis.watchyourstocks.model.Account
import io.wybis.watchyourstocks.model.Branch
import io.wybis.watchyourstocks.model.Product
import io.wybis.watchyourstocks.model.User
import io.wybis.watchyourstocks.service.exceptions.ModelAlreadyExistException

interface AccountService {

    void add(SessionDto sessionUser, Account model) throws ModelAlreadyExistException

    void onBranchCreate(SessionDto sessionUser, Branch branch);

    void onProductCreate(SessionDto sessionUser, Product product)

    void onEmployeeCreate(SessionDto sessionUser, User employee)

    void onBrokerCreate(SessionDto sessionUser, User broker)

    void onInvestorCreate(SessionDto sessionUser, User investor)

}
