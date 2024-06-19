package com.gr8idea.facebook_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.gr8idea.facebook_clone.user", "com.gr8idea.facebook_clone.mail", "com.gr8idea.facebook_clone.exception", "com.gr8idea.facebook_clone.config", "com.gr8idea.facebook_clone.auth"})
public class FacebookCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacebookCloneApplication.class, args);
	}

}
