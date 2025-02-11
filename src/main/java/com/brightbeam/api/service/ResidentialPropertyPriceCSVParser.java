package com.brightbeam.api.service;

import com.brightbeam.api.dto.ResidentialPropertyPrice;
import com.brightbeam.api.util.DateConverterUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResidentialPropertyPriceCSVParser {
	
	private final Logger log = LoggerFactory.getLogger(ResidentialPropertyPriceCSVParser.class);
	
	private final static char COMMA_SEPARATOR = ',';

	// Using ConcurrentLinkedQueue for thread-safe collection
	private static final ConcurrentLinkedQueue<ResidentialPropertyPrice> processedRecords = new ConcurrentLinkedQueue<>();

	/**
	 * Read property CSV file, process the file and convert to class object Process
	 * the records in parallel
	 * If Header line need to skip add value 1, If don't want to skip put the value 0
	 * CSV file, need to specify separator.
	 * Process in parallel and collect results in List
	 * Convert queue to list for return a List<ResidentialPropertyPrice> object
	 * Clear the queue for next use
	 * @param filePath
	 */
	public List<ResidentialPropertyPrice> readCsvFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			log.error("File does not exist: {}", filePath);
			return null;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file));
				CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1)
						.withCSVParser(new CSVParserBuilder().withSeparator(COMMA_SEPARATOR) 
								.withIgnoreQuotations(false).build())
						.build()) {

			List<String[]> readAllRecords = csvReader.readAll();

			readAllRecords.parallelStream().forEach(ResidentialPropertyPriceCSVParser::processPriceRecord);

			
			List<ResidentialPropertyPrice> resultList = new ArrayList<>(processedRecords);
			
			processedRecords.clear(); 

			return resultList;

		} catch (IOException e) {
			log.error("Error reading ResidentialPropertyPrice CSV file: " + e.getMessage());
			throw new RuntimeException(e);
		} catch (CsvException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Record processing logic, and adding to ResidentialPropertyPrice object
	 * Add to concurrent queue
	 * 
	 * @param record
	 */
	private static void processPriceRecord(String[] record) {
		ResidentialPropertyPrice residentialPropertyPrice = new ResidentialPropertyPrice();
		residentialPropertyPrice.setDateOfSale(DateConverterUtil.convertStringToDate(record[0]));
		residentialPropertyPrice.setAddress(record[1]);
		residentialPropertyPrice.setStreetName(record[2]);
		residentialPropertyPrice.setPrice(cleanAndConvertPrice(record[3]));
		
		processedRecords.offer(residentialPropertyPrice);
	}

	/**
	 * Removing currency symbols and Convert price to double
	 * 
	 * @param value
	 * @return
	 */
	private static double cleanAndConvertPrice(String value) {
		double propertyPrice = 0.0;
		try {
			String cleanedValue = value.replaceAll("[^0-9.]", "");
			propertyPrice = Double.parseDouble(cleanedValue);
	
		} catch (NumberFormatException e) {
            throw new RuntimeException(e);
		}
		return propertyPrice;
	}
}
