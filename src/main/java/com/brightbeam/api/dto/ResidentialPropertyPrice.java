package com.brightbeam.api.dto;

import java.util.Date;
import java.util.Objects;

public class ResidentialPropertyPrice {
    private String address;
    private String streetName;
    private double price;

    private Date dateOfSale;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    @Override
    public String toString() {
        return "ResidentialPropertyPrice{" +
                "address='" + address + '\'' +
                ", streetName='" + streetName + '\'' +
                ", price=" + price +
                ", dateOfSale=" + dateOfSale +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResidentialPropertyPrice that = (ResidentialPropertyPrice) o;
        return Double.compare(price, that.price) == 0 && Objects.equals(address, that.address) && Objects.equals(streetName, that.streetName) && Objects.equals(dateOfSale, that.dateOfSale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, streetName, price, dateOfSale);
    }
}
