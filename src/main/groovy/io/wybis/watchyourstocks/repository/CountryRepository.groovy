package io.wybis.watchyourstocks.repository

import io.wybis.watchyourstocks.model.Country
import org.springframework.data.jpa.repository.JpaRepository

public interface CountryRepository extends JpaRepository<Country, String> {

}
