package io.wybis.watchyourstocks.repository

import io.wybis.watchyourstocks.model.User
import org.springframework.data.jpa.repository.JpaRepository

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByTypeAndBranchId(String type, long branchId)

    User findByUserId(String userId);

}
