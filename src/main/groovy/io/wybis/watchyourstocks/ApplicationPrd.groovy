package io.wybis.watchyourstocks

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!dev")
public class ApplicationPrd {

}
