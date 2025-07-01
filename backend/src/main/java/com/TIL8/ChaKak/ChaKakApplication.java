package com.TIL8.ChaKak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChaKakApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChaKakApplication.class, args);
	}
}
