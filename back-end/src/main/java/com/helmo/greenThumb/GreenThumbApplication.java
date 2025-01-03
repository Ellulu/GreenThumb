package com.helmo.greenThumb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GreenThumbApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenThumbApplication.class, args);
	}

}