package io.wybis.watchyourstocks.repository;

import io.wybis.watchyourstocks.model.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigRepository extends JpaRepository<AppConfig, String> {

}
