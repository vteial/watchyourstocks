package io.wybis.watchyourstocks.repository

import io.wybis.watchyourstocks.model.Account
import org.springframework.data.jpa.repository.JpaRepository

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByBranchIdAndName(long branchId, String name);

}
