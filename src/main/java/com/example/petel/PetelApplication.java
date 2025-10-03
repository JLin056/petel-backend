package com.example.petel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class PetelApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetelApplication.class, args);
	}

}
