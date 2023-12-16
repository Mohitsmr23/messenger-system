package com.messengersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class MessengerSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessengerSystemApplication.class, args);
	}

}
