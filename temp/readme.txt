package io.wybis

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.support.SpringBootServletInitializer

class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		application.sources(StockmonsterApplication)
	}

}

package io.wybis

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class StockmonsterApplication {

	static void main(String[] args) {
		SpringApplication.run StockmonsterApplication, args
	}
}

package io.wybis

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner)
@SpringBootTest
class StockmonsterApplicationTests {

	@Test
	void contextLoads() {
	}

}
