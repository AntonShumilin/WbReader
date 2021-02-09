package com.WbReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WbReaderApplication {

	public static void main(String[] args) {

		SpringApplication.run(WbReaderApplication.class, args);
	}

	@Bean
	public Logger log() {
		Logger logger = LoggerFactory.getLogger("com.WbReader");
		return logger;
	}

}
