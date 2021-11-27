package com.members;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MembersApplication {

	public static void main(String[] args) {
		log.info("Start******************************");
		SpringApplication.run(MembersApplication.class, args);
	}

}
