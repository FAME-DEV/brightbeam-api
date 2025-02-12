package com.brightbeam.api.service;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.brightbeam.api.dto.PropertyStreetMapping;
import com.brightbeam.api.dto.ResidentialPropertyPrice;
import com.brightbeam.api.service.PropertyDataProcessor.PropertyAverages;

@Component
public class PropertyDataProcessorExecutor {

	private static final Logger log = LoggerFactory.getLogger(PropertyDataProcessorExecutor.class);

	@Autowired
	private NestedStreetDataParser nestedStreetDataParser;
	@Autowired
	private ResidentialPropertyPriceCSVParser residentialPropertyPriceCSVParser;
	@Autowired
	private PropertyDataProcessor propertyDataProcessor;
	
	
	@Value("${street_trees_json_path}")
	private String dublinTreePathDefault;
	
	@Value("${property_price_csv_path}")
	private String dublinPropertyPathDefault;
	@Autowired	
	private EnvConfigService envConfigService;

	public PropertyDataProcessorExecutor() {
	
	}

	public void processDataToCalculateAvergaeCost() {
		
		String dublinTreePath = ""; 
		String dublinPropertyPathCSV = "";

	
		log.info("Processing files from following paths:");
		log.info(" User set the file path for Tree is : {} " , envConfigService.getDublinTreePath());
		log.info(" User set the file path for Property Price is: {}" , envConfigService.getDublinPropertyPath());
		
		dublinTreePath = envConfigService.getDublinTreePath();
		dublinPropertyPathCSV = envConfigService.getDublinPropertyPath();
		
		 //just in case user not set the files path
	     if(dublinTreePath == null || dublinTreePath == "")
	     {
	    	 log.info("User no set the path, taking default files from API data folder " + dublinTreePathDefault);
	    	 dublinTreePath = dublinTreePathDefault;
	     }
	     if(dublinPropertyPathCSV == null || dublinPropertyPathCSV == "")
	     {
	    	 log.info("User no set the path, taking default files from API data folder " + dublinPropertyPathDefault);
	    	 dublinPropertyPathCSV = dublinPropertyPathDefault;
	     }


		File file = new File(dublinTreePath);
		PropertyStreetMapping propertyStreetMapping = nestedStreetDataParser.parseTreesJson(file);

		List<ResidentialPropertyPrice> propertiesPrice = residentialPropertyPriceCSVParser.readCsvFile(dublinPropertyPathCSV);

		PropertyAverages averages = propertyDataProcessor.calculateAveragesCost(propertiesPrice, propertyStreetMapping);
		
		log.info("--------------------------------------------------------------");
		log.info("--------------------------------------------------------------");

		log.info("-----------Average cost of a property result -----------------");

		log.info("Properties with tall trees {}: Average cost : €{}", averages.getTallTreeCount(),
				averages.getTallTreeAverage());
		log.info("Properties with short trees {}: Average cost : €{}", averages.getShortTreeCount(),
				averages.getShortTreeAverage());
		log.info("----------------------------------------------------------------");
		log.info("----------------------------------------------------------------");

	}

}
