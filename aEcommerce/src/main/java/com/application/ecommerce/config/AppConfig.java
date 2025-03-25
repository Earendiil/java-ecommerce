package com.application.ecommerce.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

	@Bean // we need as bean in order to be able to Autowire it anywhere
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
