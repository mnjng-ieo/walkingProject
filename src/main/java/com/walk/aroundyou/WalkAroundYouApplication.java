package com.walk.aroundyou;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class WalkAroundYouApplication implements CommandLineRunner{

	
	public static void main(String[] args) {
		SpringApplication.run(WalkAroundYouApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		
	}
}
