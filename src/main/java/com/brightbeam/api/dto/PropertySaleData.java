package com.brightbeam.api.dto;

import java.util.Date;
import java.util.Objects;

public class PropertySaleData {

	public PropertySaleData() {

	}

	public PropertySaleData(Double price, Date saleDate) {
		this.price = price;
		this.saleDate = saleDate;
	}

	private Double price;
	private Date saleDate;

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(price, saleDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertySaleData other = (PropertySaleData) obj;
		return Objects.equals(price, other.price) && Objects.equals(saleDate, other.saleDate);
	}

	@Override
	public String toString() {
		return "PropertySaleData [price=" + price + ", saleDate=" + saleDate + "]";
	}

}
