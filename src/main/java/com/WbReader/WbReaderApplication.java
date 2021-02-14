package com.WbReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class WbReaderApplication {

	public static void main(String[] args) {

		SpringApplication.run(WbReaderApplication.class, args);
	}

	@Bean
	public Logger log() {
		return LoggerFactory.getLogger("com.WbReader");
	}
}
