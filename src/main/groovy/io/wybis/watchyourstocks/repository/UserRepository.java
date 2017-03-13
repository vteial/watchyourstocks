package io.wybis.watchyourstocks.repository;

import io.wybis.watchyourstocks.model.AutoNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoNumberRepository extends JpaRepository<AutoNumber, String> {

}
