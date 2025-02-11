package com.brightbeam.api.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Address {
	
    private final Map<String, Object> propertiesAddress;
    private Integer streetNumber;

    public Address() {
        this.propertiesAddress = new HashMap<>();
    }

    public Address(Integer streetNumber) {
        this.propertiesAddress = new HashMap<>();
        this.streetNumber = streetNumber;
    }

    public void addProperty(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Property key cannot be null");
        }
        this.propertiesAddress.put(key, value);
    }

    public Object getProperty(String key) {
        return propertiesAddress.get(key);
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public boolean isLeafNode() {
        return streetNumber != null;
    }

    public Map<String, Object> getAllProperties() {
        return new HashMap<>(propertiesAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(propertiesAddress, address.propertiesAddress) &&
                Objects.equals(streetNumber, address.streetNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertiesAddress, streetNumber);
    }

    @Override
    public String toString() {
        if (isLeafNode()) {
            return "Address{value=" + streetNumber + '}';
        }
        return "Address{properties=" + propertiesAddress + '}';
    }
}
