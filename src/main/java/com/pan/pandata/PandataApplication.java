package com.pan.pandata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PandataApplication {

	public static void main(String[] args) {
		SpringApplication.run(PandataApplication.class, args);
		System.out.println("Pan Service Running on 8088");
	}

}
