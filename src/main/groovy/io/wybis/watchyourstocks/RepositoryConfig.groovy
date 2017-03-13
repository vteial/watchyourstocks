package io.wybis.watchyourstocks

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.JdbcTemplate

import javax.sql.DataSource

@EnableJpaRepositories
@Configuration
public class RepositoryConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder.create().build();

        return dataSource;
    }

    JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource());

        return jdbcTemplate;
    }

}
