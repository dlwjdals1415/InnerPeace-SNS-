package com.social.innerPeace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class InnerPeaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InnerPeaceApplication.class, args);
	}

}
