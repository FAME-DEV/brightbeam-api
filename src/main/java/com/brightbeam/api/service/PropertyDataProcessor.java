/**
 * 
 */
package com.brightbeam.api.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.brightbeam.api.dto.Address;
import com.brightbeam.api.dto.HeightCategory;
import com.brightbeam.api.dto.PropertySaleData;
import com.brightbeam.api.dto.ResidentialPropertyPrice;
import com.brightbeam.api.dto.StreetType;
import com.brightbeam.api.exception.ValidationException;
import com.brightbeam.api.dto.PropertyStreetMapping;

/**
 * 
 */
@Service
public class PropertyDataProcessor {

	private final Logger log = LoggerFactory.getLogger(PropertyDataProcessor.class);

	/**
	 * In this method, calculate the average cost of a property, method will take
	 * two parameters(propertiesPrice list and PropertyStreetMapping with categories
	 * map) as input
	 * 
	 * @param propertiesPrice
	 * @param propertyStreetMapping
	 * @return
	 */
	public PropertyAverages calculateAveragesCost(List<ResidentialPropertyPrice> propertiesPrice,
			PropertyStreetMapping propertyStreetMapping) {

		log.info("Starting average price calculation");
		
		validateInputsData(propertiesPrice, propertyStreetMapping);

		Map<String, HeightCategory> heightCategories = propertyStreetMapping.getAllHeightCategories();
		
		Map<String, List<PropertySaleData>> streetPrices = createPriceLookupMap(propertiesPrice);

		log.info("Created street prices lookup map with " + streetPrices.size() + " entries");

		double tallTreeTotal = 0;
		int tallTreeCount = 0;
		double shortTreeTotal = 0;
		int shortTreeCount = 0;

		// Start to process tall trees
		HeightCategory tallCategory = heightCategories.get("tall");
		if (tallCategory != null) {
			for (StreetType streetType : tallCategory.getAllStreetTypes().values()) {
				for (Address address : streetType.getAllLocations().values()) {
					for (Map.Entry<String, Object> addressEntry : address.getAllProperties().entrySet()) {
						String streetName = addressEntry.getKey();
//						Double price = streetPrices.get(streetName);
						List<PropertySaleData> sales = streetPrices.get(streetName);
                        Double price = getMostRelevantPrice(sales);
						if (price != null) {
							tallTreeTotal += price;
							tallTreeCount++;
						} else {
							log.debug("No price found for street : {}", streetName);
						}
					}
				}
			}
		}

		// Start to process short trees
		HeightCategory shortCategory = heightCategories.get("short");
		if (shortCategory != null) {
			for (StreetType streetType : shortCategory.getAllStreetTypes().values()) {
				for (Address address : streetType.getAllLocations().values()) {
					for (Map.Entry<String, Object> addressEntry : address.getAllProperties().entrySet()) {
						String streetName = addressEntry.getKey();
//						Double price = streetPrices.get(streetName);
						List<PropertySaleData> sales = streetPrices.get(streetName);
                        Double price = getMostRelevantPrice(sales);
						if (price != null) {
							shortTreeTotal += price;
							shortTreeCount++;
						} else {
							log.debug("No price found for street : {}", streetName);
						}
					}
				}
			}
		}

		// Results validations
		if (tallTreeCount == 0 && shortTreeCount == 0) {
			throw new ValidationException("No valid properties found for calculation");
		}

		double tallTreeAverage = tallTreeCount > 0 ? tallTreeTotal / tallTreeCount : 0;
		double shortTreeAverage = shortTreeCount > 0 ? shortTreeTotal / shortTreeCount : 0;

		log.info(String.format("Cost calculation completed. Tall trees: %d properties, Short trees: %d properties",
				tallTreeCount, shortTreeCount));
		log.info("Tall trees : €{} Average cost , Short trees: €{} average cost",
				tallTreeAverage, shortTreeAverage);

		return new PropertyAverages(tallTreeAverage, shortTreeAverage, tallTreeCount, shortTreeCount);
	}

	private static void validateInputsData(List<ResidentialPropertyPrice> propertiesPrice,PropertyStreetMapping propertyStreetMapping) {
		if (propertiesPrice == null) {
			throw new ValidationException("Properties price list cannot be null");
		}
		if(propertyStreetMapping == null) {
			throw new ValidationException("propertyStreetMapping object cannot be null");
		}
		if (propertiesPrice.isEmpty()) {
			throw new ValidationException("Properties price list cannot be empty");
		}
		if (propertyStreetMapping.getAllHeightCategories().isEmpty()) {
			throw new ValidationException("Height categories map cannot be empty");
		}

	}
	
	/**
	 * In This method will created a price lookup map to manage multiple sales dates efficiently
	 * and validate the sale data before processing
	 * Add valid sale data entries to the map
	 * Sort sales by date for each street, 
	 * ensuring the most recent sales appear first
	 * @param propertiesPrice
	 * @return
	 */
    public  Map<String, List<PropertySaleData>> createPriceLookupMap(
            List<ResidentialPropertyPrice> propertiesPrice) {
        
        Map<String, List<PropertySaleData>> streetPrices = new HashMap<>();
        
        for (ResidentialPropertyPrice property : propertiesPrice) {
            String streetName = property.getStreetName();
            Double price = property.getPrice();
            Date saleDate = property.getDateOfSale();

            if (streetName == null || price == null || saleDate == null) {
                log.error("Invalid sale data found: " + 
                             "Street: " + streetName + 
                             ", Price: " + price + 
                             ", Date: " + saleDate);
                continue;
            }

            streetPrices.computeIfAbsent(streetName, k -> new ArrayList<>())
                       .add(new PropertySaleData(price, saleDate));
        }

        for (List<PropertySaleData> sales : streetPrices.values()) {
            sales.sort((a, b) -> b.getSaleDate().compareTo(a.getSaleDate()));
        }

        return streetPrices;
    }
    
    /**
     * This method used to get the most appropriate price, 
     * as price is already sorted,
     * validate price should not null or empty
     * @param sales
     * @return
     */
	private static Double getMostRelevantPrice(List<PropertySaleData> sales) {
		if (sales == null || sales.isEmpty()) {
			return null;
		}
		return sales.get(0).getPrice();

	}


	public static class PropertyAverages {

		private final double tallTreeAverage;
		private final double shortTreeAverage;
		private final int tallTreeCount;
		private final int shortTreeCount;

		public PropertyAverages(double tallTreeAverage, double shortTreeAverage, int tallTreeCount,
				int shortTreeCount) {
			this.tallTreeAverage = tallTreeAverage;
			this.shortTreeAverage = shortTreeAverage;
			this.tallTreeCount = tallTreeCount;
			this.shortTreeCount = shortTreeCount;
		}

		public double getTallTreeAverage() {
			return tallTreeAverage;
		}

		public double getShortTreeAverage() {
			return shortTreeAverage;
		}

		public int getTallTreeCount() {
			return tallTreeCount;
		}

		public int getShortTreeCount() {
			return shortTreeCount;
		}
	}

}
