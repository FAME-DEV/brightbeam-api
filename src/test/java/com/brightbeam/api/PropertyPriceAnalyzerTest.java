package com.brightbeam.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.brightbeam.api.dto.Address;
import com.brightbeam.api.dto.HeightCategory;
import com.brightbeam.api.dto.PropertySaleData;
import com.brightbeam.api.dto.PropertyStreetMapping;
import com.brightbeam.api.dto.ResidentialPropertyPrice;
import com.brightbeam.api.dto.StreetType;
import com.brightbeam.api.service.PropertyDataProcessor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class PropertyPriceAnalyzerTest {
    @Mock
    private ResidentialPropertyPrice mockProperty;
    
    @Mock
    private HeightCategory tallCategory;
    
    @Mock
    private HeightCategory shortCategory;
    
    @Mock
    private StreetType streetType;

    private List<ResidentialPropertyPrice> propertiesPrice;
    private Map<String, HeightCategory> heightCategories;
    private PropertyStreetMapping propertyStreetMapping;
    private Date baseDate;

    @BeforeEach
    void setUp() {
    	Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        baseDate = calendar.getTime();
        
        propertiesPrice = new ArrayList<>();
        heightCategories = new HashMap<>();
        propertyStreetMapping = new PropertyStreetMapping();
    }
    
    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    @Test
    void testPriceLookupMapCreation() {
        // Create and configure mocks for multiple sales
        ResidentialPropertyPrice property1 = mock(ResidentialPropertyPrice.class);
        when(property1.getStreetName()).thenReturn("Oak St");
        when(property1.getPrice()).thenReturn(100000.0);
        when(property1.getDateOfSale()).thenReturn(baseDate);

        ResidentialPropertyPrice property2 = mock(ResidentialPropertyPrice.class);
        when(property2.getStreetName()).thenReturn("Oak St");
        when(property2.getPrice()).thenReturn(110000.0);
        when(property2.getDateOfSale()).thenReturn(addDays(baseDate, 30));

        propertiesPrice.add(property1);
        propertiesPrice.add(property2);
        
        PropertyDataProcessor PropertyDataProcessor = new PropertyDataProcessor();

        Map<String, List<PropertySaleData>> result = 
        		PropertyDataProcessor.createPriceLookupMap(propertiesPrice);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get("Oak St").size());
        assertEquals(110000.0, result.get("Oak St").get(0).getPrice());
        
        // Verify dates are ordered correctly
        assertTrue(result.get("Oak St").get(0).getSaleDate().after(
                  result.get("Oak St").get(1).getSaleDate()));
    }
    
    @Test
    void testCalculateAverages() {
        // Setup property mocks with different dates
        ResidentialPropertyPrice oakProperty = mock(ResidentialPropertyPrice.class);
        when(oakProperty.getStreetName()).thenReturn("rathvale drive");
        when(oakProperty.getPrice()).thenReturn(100000.0);
        when(oakProperty.getDateOfSale()).thenReturn(baseDate);

        ResidentialPropertyPrice mapleProperty = mock(ResidentialPropertyPrice.class);
        when(mapleProperty.getStreetName()).thenReturn("merlyn drive");
        when(mapleProperty.getPrice()).thenReturn(200000.0);
        when(mapleProperty.getDateOfSale()).thenReturn(addDays(baseDate, 15));
        
        //glenarriff road
        
        ResidentialPropertyPrice glenarriffRoad = mock(ResidentialPropertyPrice.class);
        when(glenarriffRoad.getStreetName()).thenReturn("glenarriff road");
        when(glenarriffRoad.getPrice()).thenReturn(500000.0);
        when(glenarriffRoad.getDateOfSale()).thenReturn(addDays(baseDate, 15));

        propertiesPrice.add(oakProperty);
        propertiesPrice.add(mapleProperty);
        propertiesPrice.add(glenarriffRoad);
        
        PropertyDataProcessor propertyDataProcessor = new PropertyDataProcessor();
        PropertyStreetMapping propertyStreetMappingData = createTestData();

        PropertyDataProcessor.PropertyAverages averages = 
        		propertyDataProcessor.calculateAveragesCost(propertiesPrice, propertyStreetMappingData);

        assertNotNull(averages);
        assertEquals(500000.0, averages.getTallTreeAverage(), 0.01);
        assertEquals(150000.0, averages.getShortTreeAverage(), 0.01);
        assertEquals(1, averages.getTallTreeCount());
      
    }
    
    public static PropertyStreetMapping createTestData() {
    	
    	PropertyStreetMapping propertyStreetMapping = new PropertyStreetMapping();
        
        // Create 'drive' street type
    	 HeightCategory shortCategory = new HeightCategory();
        StreetType driveStreetType = new StreetType();
        
        // Abbey Drive
        Address abbeyAddress = new Address(0);
        abbeyAddress.addProperty("abbey drive", 0);
        driveStreetType.addLocation("abbey", abbeyAddress);
        
        // Coolrua Drive
        Address coolruaAddress = new Address(10);
        coolruaAddress.addProperty("coolrua drive", 10);
        driveStreetType.addLocation("coolrua", coolruaAddress);
        
        // Coultry Drive
        Address coultryAddress = new Address(5);
        coultryAddress.addProperty("coultry drive", 5);
        driveStreetType.addLocation("coultry", coultryAddress);
        
        // Drumcliffe Drive
        Address drumcliffeAddress = new Address(0);
        drumcliffeAddress.addProperty("drumcliffe drive", 0);
        driveStreetType.addLocation("drumcliffe", drumcliffeAddress);
        
        // Grove Park Drive
        Address groveAddress = new Address(10);
        groveAddress.addProperty("grove park drive", 10);
        driveStreetType.addLocation("park", groveAddress);
        
        // McAuley Drive
        Address mcauleyAddress = new Address(10);
        mcauleyAddress.addProperty("mcauley drive", 10);
        driveStreetType.addLocation("mcauley", mcauleyAddress);
        
        // Merlyn Drive
        Address merlynAddress = new Address(5);
        merlynAddress.addProperty("merlyn drive", 5);
        driveStreetType.addLocation("merlyn", merlynAddress);
        
        // Merylin Drive
        Address merylinAddress = new Address(5);
        merylinAddress.addProperty("merylin drive", 5);
        driveStreetType.addLocation("merylin", merylinAddress);
        
        // Rathvale Drive
        Address rathvaleAddress = new Address(10);
        rathvaleAddress.addProperty("rathvale drive", 10);
        driveStreetType.addLocation("rathvale", rathvaleAddress);
        
        // Rathvilly Drive
        Address rathvillyAddress = new Address(10);
        rathvillyAddress.addProperty("rathvilly drive", 10);
        driveStreetType.addLocation("rathvilly", rathvillyAddress);
        
        // St Marys Drive
        Address stMarysAddress = new Address(10);
        stMarysAddress.addProperty("st marys drive", 10);
        driveStreetType.addLocation("marys", stMarysAddress);
        
        // St Mobhi Drive
        Address stMobhiAddress = new Address(10);
        stMobhiAddress.addProperty("st mobhi drive", 10);
        driveStreetType.addLocation("mobhi", stMobhiAddress);
        
        // St Brendens Drive
        Address stBrendensAddress = new Address(0);
        stBrendensAddress.addProperty("st brendens drive", 0);
        driveStreetType.addLocation("brendens", stBrendensAddress);
        
        // Thornville Drive
        Address thornvilleAddress = new Address(10);
        thornvilleAddress.addProperty("thornville drive", 10);
        driveStreetType.addLocation("thornville", thornvilleAddress);
        
        
        // Create HeightCategory for 'short'
        shortCategory.addStreetType("drive", driveStreetType);
        propertyStreetMapping.addHeightCategory("short", shortCategory);
        
     // Create 'tall' category
        HeightCategory tallCategory = new HeightCategory();
        
        // Create 'road' street type
        StreetType roadStreetType = new StreetType();
        
        // Create 'tall' category
        Map<String, StreetType> tallStreetTypes = new HashMap<>();
        
   
        // Adelaide Road
        Address adelaideAddress = new Address(25);
        adelaideAddress.addProperty("adelaide road", 25);
        roadStreetType.addLocation("adelaide", adelaideAddress);
        
        // Beaumont Road
        Address beaumontAddress = new Address(20);
        beaumontAddress.addProperty("beaumont road", 20);
        roadStreetType.addLocation("beaumont", beaumontAddress);
        
        // Cabra Road
        Address cabraAddress = new Address(25);
        cabraAddress.addProperty("cabra road", 25);
        roadStreetType.addLocation("cabra", cabraAddress);
        
        // Cambridge Road
        Address cambridgeAddress = new Address(20);
        cambridgeAddress.addProperty("cambridge road", 20);
        roadStreetType.addLocation("cambridge", cambridgeAddress);
        
        // Carnlough Road
        Address carnloughAddress = new Address(20);
        carnloughAddress.addProperty("carnlough road", 20);
        roadStreetType.addLocation("carnlough", carnloughAddress);
        
        // Clare Road
        Address clareAddress = new Address(25);
        clareAddress.addProperty("clare road", 25);
        roadStreetType.addLocation("clare", clareAddress);
        
        // Clogher Road
        Address clogherAddress = new Address(20);
        clogherAddress.addProperty("clogher road", 20);
        roadStreetType.addLocation("clogher", clogherAddress);
        
        // Clyde Road
        Address clydeAddress = new Address(20);
        clydeAddress.addProperty("clyde road", 20);
        roadStreetType.addLocation("clyde", clydeAddress);
        
        // Donnybrook Road
        Address donnybrookAddress = new Address(20);
        donnybrookAddress.addProperty("donnybrook road", 20);
        roadStreetType.addLocation("donnybrook", donnybrookAddress);
        
        // Drumcondra Road
        Address drumcondraAddress = new Address(25);
        drumcondraAddress.addProperty("drumcondra road", 25);
        roadStreetType.addLocation("drumcondra", drumcondraAddress);
        
        // Glenarriff Road
        Address glenarriffAddress = new Address(20);
        glenarriffAddress.addProperty("glenarriff road", 20);
        roadStreetType.addLocation("glenarriff", glenarriffAddress);
        
        // Glenbrook Road
        Address glenbrookAddress = new Address(20);
        glenbrookAddress.addProperty("glenbrook road", 20);
        roadStreetType.addLocation("glenbrook", glenbrookAddress);
        
        // Glendhu Road
        Address glendhuAddress = new Address(20);
        glendhuAddress.addProperty("glendhu road", 20);
        roadStreetType.addLocation("glendhu", glendhuAddress);
        
        // Haddington Road
        Address haddingtonAddress = new Address(25);
        haddingtonAddress.addProperty("haddington road", 25);
        roadStreetType.addLocation("haddington", haddingtonAddress);
        
        // Inchicore Road
        Address inchicoreAddress = new Address(20);
        inchicoreAddress.addProperty("inchicore road", 20);
        roadStreetType.addLocation("inchicore", inchicoreAddress);
        
        // Infirmary Road
        Address infirmaryAddress = new Address(25);
        infirmaryAddress.addProperty("infirmary road", 25);
        roadStreetType.addLocation("infirmary", infirmaryAddress);
        
        // Infirmery Road
        Address infirmeryAddress = new Address(20);
        infirmeryAddress.addProperty("infirmery road", 20);
        roadStreetType.addLocation("infirmery", infirmeryAddress);
        
        // Iona Road
        Address ionaAddress = new Address(20);
        ionaAddress.addProperty("iona road", 20);
        roadStreetType.addLocation("iona", ionaAddress);
        
        // North Circular Road
        Address northCircularAddress = new Address(25);
        northCircularAddress.addProperty("north circular road", 25);
        roadStreetType.addLocation("circular", northCircularAddress);
        
        // South Circular Road
        Address southCircularAddress = new Address(20);
        southCircularAddress.addProperty("circular", southCircularAddress);
        roadStreetType.addLocation("circular", southCircularAddress);
        
        // Orchard Road
        Address orchardAddress = new Address(25);
        orchardAddress.addProperty("orchard road", 25);
        roadStreetType.addLocation("orchard", orchardAddress);
        
        tallStreetTypes.put("road", roadStreetType);
        
        tallCategory.addStreetType("road", roadStreetType);
        
        // Create 'green' street type
        StreetType greenStreetType = new StreetType();
        
        
        // Beresford Green
        Address beresfordAddress = new Address(20);
        beresfordAddress.addProperty("beresford green", 20);
        greenStreetType.addLocation("beresford", beresfordAddress);
        
        // Colege Green
        Address colegeAddress = new Address(20);
        colegeAddress.addProperty("colege green", 20);
        greenStreetType.addLocation("colege", colegeAddress);
        
        // College Green
        Address collegeAddress = new Address(20);
        collegeAddress.addProperty("college green", 20);
        greenStreetType.addLocation("college", collegeAddress);
        
        // Fairways Green
        Address fairwaysAddress = new Address(25);
        fairwaysAddress.addProperty("fairways green", 25);
        greenStreetType.addLocation("fairways", fairwaysAddress);
        
        tallStreetTypes.put("green", greenStreetType);
        
        // Create HeightCategory for 'tall'
        tallCategory.addStreetType("green", greenStreetType);
        propertyStreetMapping.addHeightCategory("tall", tallCategory);
     
        
        return propertyStreetMapping;
    }
    
}

