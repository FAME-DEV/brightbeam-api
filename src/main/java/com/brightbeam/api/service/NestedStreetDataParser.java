package com.brightbeam.api.service;

import com.brightbeam.api.dto.Address;
import com.brightbeam.api.dto.HeightCategory;
import com.brightbeam.api.dto.StreetType;
import com.brightbeam.api.dto.PropertyStreetMapping;
import com.brightbeam.api.exception.DataParsingException;
import com.brightbeam.api.exception.ValidationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NestedStreetDataParser {
	
	private final Logger LOGGER = LoggerFactory.getLogger(NestedStreetDataParser.class);
			
    private final ObjectMapper objectMapper;
    private static final int MAX_DEPTH = 10; // Prevent stack overflow from malicious JSON
    private static final int MAX_VALUE = 100; // Maximum allowed value for height
    private static final int MIN_VALUE = 0;   // Minimum allowed value for height

    public NestedStreetDataParser() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Validate input JSON file
     * Read and parse the JSON file directly using ObjectMapper
     * 
     * @param streetDataJsonFile
     * @return
     * @throws DataParsingException
     */
    public PropertyStreetMapping parseTreesJson(File streetDataJsonFile) throws DataParsingException {
        try {
        	
        	LOGGER.info("Starting parsing the tree JSON file");
        	
            validateInputFile(streetDataJsonFile);
            
            JsonNode rootNode = objectMapper.readTree(streetDataJsonFile);
            
            validateRootStructure(rootNode);
            return parseLocationData(rootNode);
        } catch (IOException e) {
            throw new DataParsingException("Failed to parse JSON content", e);
        }
    }

    /**
     * Perform the validations on the JSON file
     * @param streetDataJsonFile
     * @throws ValidationException
     */
    private void validateInputFile(File streetDataJsonFile) throws ValidationException {
    	
    	LOGGER.info("validate JSON file starting");
        
        if (streetDataJsonFile == null) {
            throw new DataParsingException("JSON file cannot be null");
        }
        if (!streetDataJsonFile.exists()) {
            throw new DataParsingException("JSON file does not exist: " + streetDataJsonFile.getAbsolutePath());
        }
        if (!streetDataJsonFile.canRead()) {
            throw new DataParsingException("Cannot read JSON file: " + streetDataJsonFile.getAbsolutePath());
        }
    }

    /**
     * Validate the JSON file structure and Verify that only 'short' and 'tall' categories exist
     * @param rootNode
     * @throws ValidationException
     */
    private void validateRootStructure(JsonNode rootNode) throws ValidationException {
        if (!rootNode.isObject()) {
            throw new ValidationException("Root JSON must be an object");
        }

        if (!rootNode.has("short") && !rootNode.has("tall")) {
            throw new ValidationException("JSON must contain at least one height category (short/tall)");
        }

        for (Iterator<String> it = rootNode.fieldNames(); it.hasNext(); ) {
            String fieldName = it.next();
            if (!fieldName.equals("short") && !fieldName.equals("tall")) {
                throw new ValidationException("Invalid height category: " + fieldName);
            }
        }
    }

    private PropertyStreetMapping parseLocationData(JsonNode rootNode) throws DataParsingException {
    	LOGGER.info("Starting parsing the location/address data");
    	
        PropertyStreetMapping propertyStreetMapping = new PropertyStreetMapping();

        try {
            Iterator<Map.Entry<String, JsonNode>> heightCategories = rootNode.fields();
            while (heightCategories.hasNext()) {
                Map.Entry<String, JsonNode> heightCategory = heightCategories.next();
                String heightType = heightCategory.getKey();
                JsonNode categoryData = heightCategory.getValue();

                validateHeightCategory(heightType, categoryData);
                HeightCategory category = processHeightCategory(categoryData, 0);
                propertyStreetMapping.addHeightCategory(heightType, category);
            }
        } catch (Exception e) {
            throw new DataParsingException("Error parsing location data", e);
        }

        return propertyStreetMapping;
    }

    /**
     * Perform validations against height type, JSON object is available or not
     * @param heightType
     * @param categoryData
     * @throws ValidationException
     */
    private void validateHeightCategory(String heightType, JsonNode categoryData) throws ValidationException {
        if (!categoryData.isObject()) {
            throw new ValidationException("Height category '" + heightType + "' must be an object");
        }
    }

    /**
     * This method perform the parsing for child nodes of heights
     * @param categoryNode
     * @param depth
     * @return
     * @throws DataParsingException
     */
    private HeightCategory processHeightCategory(JsonNode categoryNode, int depth) throws DataParsingException {
    	
    	LOGGER.info("Starting processHeightCategory data");
        validateDepth(depth);
        HeightCategory category = new HeightCategory();

        try {
            Iterator<Map.Entry<String, JsonNode>> streetTypes = categoryNode.fields();
            while (streetTypes.hasNext()) {
                Map.Entry<String, JsonNode> streetType = streetTypes.next();
                String type = streetType.getKey();
                JsonNode streetData = streetType.getValue();

                validateStreetType(type, streetData);
                StreetType streetTypeObj = processStreetType(streetData, depth + 1);
                category.addStreetType(type, streetTypeObj);
            }
        } catch (Exception e) {
            throw new DataParsingException("Error processing height category", e);
        }

        return category;
    }

    /**
     * Perform validations on Street type JSON object 
     * @param type
     * @param streetData
     * @throws ValidationException
     */
    private void validateStreetType(String type, JsonNode streetData) throws ValidationException {
        if (!streetData.isObject()) {
            throw new ValidationException("Street type '" + type + "' must be an object");
        }
        if (type.trim().isEmpty()) {
            throw new ValidationException("Street type name cannot be empty");
        }
    }

    /**
     * This method parse the street JSON nodes
     * @param streetNode
     * @param depth
     * @return
     * @throws DataParsingException
     */
    private StreetType processStreetType(JsonNode streetNode, int depth) throws DataParsingException {
    	
    	LOGGER.debug("Starting processStreetType data");
        validateDepth(depth);
        StreetType streetType = new StreetType();

        try {
            Iterator<Entry<String, JsonNode>> locations = streetNode.fields();
            while (locations.hasNext()) {
                Entry<String, JsonNode> location = locations.next();
                String locationName = location.getKey();
                JsonNode locationData = location.getValue();

                validateLocation(locationName, locationData);
                Address addressObj = processLocation(locationData, depth + 1);
                streetType.addLocation(locationName, addressObj);
            }
        } catch (Exception e) {
            throw new DataParsingException("Error processing street type", e);
        }

        return streetType;
    }

    private void validateLocation(String locationName, JsonNode locationData) throws ValidationException {
        if (locationName.trim().isEmpty()) {
            throw new ValidationException("Location name cannot be empty");
        }
        if (!locationData.isObject() && !locationData.isNumber()) {
            throw new ValidationException("Location '" + locationName + "' must be either an object or a number");
        }
    }

    /**
     * This method parse the Street Address and add into address object
     * @param locationNode
     * @param depth
     * @return
     * @throws DataParsingException
     */
    private Address processLocation(JsonNode locationNode, int depth) throws DataParsingException {
        validateDepth(depth);
        Address address = new Address();

        try {
            if (locationNode.isObject()) {
                processLocationObject(address, locationNode, depth);
            } else if (locationNode.isNumber()) {
                validateLocationValue(locationNode.asInt());
                address.setStreetNumber(locationNode.asInt());
            }
        } catch (Exception e) {
            throw new DataParsingException("Error processing location", e);
        }

        return address;
    }

    private void processLocationObject(Address address, JsonNode locationNode, int depth) throws DataParsingException {
        Iterator<Entry<String, JsonNode>> properties = locationNode.fields();
        while (properties.hasNext()) {
            Entry<String, JsonNode> property = properties.next();
            String propertyName = property.getKey();
            JsonNode propertyValue = property.getValue();

            validateLocationProperty(propertyName, propertyValue);

            if (propertyValue.isNumber()) {
                validateLocationValue(propertyValue.asInt());
                address.addProperty(propertyName, new Address(propertyValue.asInt()));
            } else {
                address.addProperty(propertyName, processLocation(propertyValue, depth + 1));
            }
        }
    }

    private void validateLocationValue(int value) throws ValidationException {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new ValidationException(
                    String.format("Location value must be between %d and %d, found: %d",
                            MIN_VALUE, MAX_VALUE, value));
        }
    }

    private void validateDepth(int depth) throws ValidationException {
        if (depth > MAX_DEPTH) {
            throw new ValidationException("JSON structure exceeds maximum allowed depth of " + MAX_DEPTH);
        }
    }

    private void validateLocationProperty(String propertyName, JsonNode propertyValue) throws ValidationException {
        if (propertyName.trim().isEmpty()) {
            throw new ValidationException("Property name cannot be empty");
        }
        if (!propertyValue.isObject() && !propertyValue.isNumber()) {
            throw new ValidationException("Property '" + propertyName + "' must be either an object or a number");
        }
    }
}
