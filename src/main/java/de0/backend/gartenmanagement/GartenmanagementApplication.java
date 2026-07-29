package de0.backend.gartenmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GartenmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(GartenmanagementApplication.class, args);
	}

}
