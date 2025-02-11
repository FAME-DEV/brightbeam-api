package com.brightbeam.api.dto;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class PropertyStreetMapping {
	
    private Map<String, HeightCategory> heightCategories;

    public PropertyStreetMapping() {
        this.heightCategories = new HashMap<>();
    }

    public void addHeightCategory(String height, HeightCategory category) {
        if (height == null || category == null) {
            throw new IllegalArgumentException("Height and category cannot be null");
        }
        this.heightCategories.put(height, category);
    }

    public HeightCategory getHeightCategory(String height) {
        return heightCategories.get(height);
    }

    public Map<String, HeightCategory> getAllHeightCategories() {
        return new HashMap<>(heightCategories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyStreetMapping that = (PropertyStreetMapping) o;
        return Objects.equals(heightCategories, that.heightCategories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heightCategories);
    }

    @Override
    public String toString() {
        return "LocationData{heightCategories=" + heightCategories + '}';
    }
}
