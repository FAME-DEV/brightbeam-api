package com.brightbeam.api.dto;
import java.util.*;

public class StreetType {
    private final Map<String, Address> address;

    public StreetType() {
        this.address = new HashMap<>();
    }

    public void addLocation(String name, Address address) {
        if (name == null || address == null) {
            throw new IllegalArgumentException("Name and location cannot be null");
        }
        this.address.put(name, address);
    }

    public Address getLocation(String name) {
        return address.get(name);
    }

    public Map<String, Address> getAllLocations() {
        return new HashMap<>(address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetType that = (StreetType) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return "StreetType{locations=" + address + '}';
    }
}
