package com.example.Qraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class QraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(QraftApplication.class, args);
	}

}
