package io.wybis.watchyourstocks.repository

import io.wybis.watchyourstocks.model.Branch
import org.springframework.data.jpa.repository.JpaRepository

public interface BranchRepository extends JpaRepository<Branch, Long> {

}
