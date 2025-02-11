package com.brightbeam.api;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.brightbeam.api.dto.PropertyStreetMapping;
import com.brightbeam.api.dto.ResidentialPropertyPrice;
import com.brightbeam.api.service.NestedStreetDataParser;
import com.brightbeam.api.service.PropertyDataProcessor;
import com.brightbeam.api.service.ResidentialPropertyPriceCSVParser;
import com.brightbeam.api.service.PropertyDataProcessor.PropertyAverages;

@SpringBootApplication
public class BrightbeamApiApplication {
	
	private static final Logger log = LoggerFactory.getLogger(BrightbeamApiApplication.class);

	public static void main(String[] args) {
				
		ConfigurableApplicationContext ctx = SpringApplication.run(BrightbeamApiApplication.class, args);
		
		 String dublinTreePath = System.getenv("DUBLIN_TREE_PATH");
	     String dublinPropertyPath = System.getenv("DUBLIN_PROPERTY_PATH");//dublin-property
	     processFiles(dublinTreePath, dublinPropertyPath);
		
		ctx.close();
	}
	
	 private static void processFiles(String dublinTreePath, String dublinPropertyPathCSV) {
	        // Your file processing logic here
		 log.info("Processing files from:");
		 log.info("- " + dublinTreePath);
		 log.info("- " + dublinPropertyPathCSV);
	        
		 log.info("Start reading");
			//String filePath = "/Users/waheedashraf/Downloads/dublin_trees.json";

			NestedStreetDataParser nestedStreetDataParser = new NestedStreetDataParser();
			File file = new File(dublinTreePath);
			PropertyStreetMapping propertyStreetMapping = nestedStreetDataParser.parseTreesJson(file);

			ResidentialPropertyPriceCSVParser residentialPropertyPriceCSVParser = new ResidentialPropertyPriceCSVParser();
			
			//String filePathCSV = "/Users/waheedashraf/Downloads/dublin-property.csv";
			List<ResidentialPropertyPrice> propertiesPrice = residentialPropertyPriceCSVParser.readCsvFile(dublinPropertyPathCSV);

			PropertyDataProcessor propertyDataProcessor = new PropertyDataProcessor();

			PropertyAverages averages = propertyDataProcessor.calculateAveragesCost(propertiesPrice, propertyStreetMapping);
			
			log.info("--------Cost result ----------------");

			log.info("Properties with tall trees {}: average cost : €{}", averages.getTallTreeCount(),
					averages.getTallTreeAverage());
			log.info("Properties with short trees {}: average cost : €{}", averages.getShortTreeCount(),
					averages.getShortTreeAverage());
	    }

}
