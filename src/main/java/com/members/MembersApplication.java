package com.members;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Slf4j
@SpringBootApplication
@EnableWebMvc
@ComponentScan(basePackages = {"com.members"} )
@EnableJpaRepositories("com.members.repository")
@EntityScan("com.members.model")
public class MembersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MembersApplication.class, args);
		log.info("Members Startup Done!");

	}

}
