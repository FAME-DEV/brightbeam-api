package com.brightbeam.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.brightbeam.api.service.PropertyDataProcessorExecutor;

@SpringBootApplication
public class BrightbeamApiApplication implements CommandLineRunner {

	private final PropertyDataProcessorExecutor propertyDataProcessorExecutor;

	public BrightbeamApiApplication(PropertyDataProcessorExecutor propertyDataProcessorExecutor) {
		this.propertyDataProcessorExecutor = propertyDataProcessorExecutor;
	}

	public static void main(String[] args) {
		SpringApplication.run(BrightbeamApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		propertyDataProcessorExecutor.processDataToCalculateAvergaeCost();

	}

}
