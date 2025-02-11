package com.brightbeam.api.dto;
import java.util.*;

public class HeightCategory {
    private final Map<String, StreetType> streetTypes;

    public HeightCategory() {
        this.streetTypes = new HashMap<>();
    }

    public void addStreetType(String type, StreetType streetType) {
        if (type == null || streetType == null) {
            throw new IllegalArgumentException("Type and street type cannot be null");
        }
        this.streetTypes.put(type, streetType);
    }

    public StreetType getStreetType(String type) {
        return streetTypes.get(type);
    }

    public Map<String, StreetType> getAllStreetTypes() {
        return new HashMap<>(streetTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeightCategory that = (HeightCategory) o;
        return Objects.equals(streetTypes, that.streetTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streetTypes);
    }

    @Override
    public String toString() {
        return "HeightCategory{streetTypes=" + streetTypes + '}';
    }
}
