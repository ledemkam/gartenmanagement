package de0.backend.gartenmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class GartenmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(GartenmanagementApplication.class, args);
	}

}
