package com.example.testdemo.bean;

import java.util.Map;

public class Place {
private Map<String,String> cities;
private Map<String,String> countries;
public Map<String, String> getCities() {
	return cities;
}
public void setCities(Map<String, String> cities) {
	this.cities = cities;
}
public Map<String, String> getCountries() {
	return countries;
}
public void setCountries(Map<String, String> countries) {
	this.countries = countries;
}
@Override
public String toString() {
	return "Place [cities=" + cities + ", countries=" + countries
			+ ", getCities()=" + getCities() + ", getCountries()="
			+ getCountries() + ", getClass()=" + getClass() + ", hashCode()="
			+ hashCode() + ", toString()=" + super.toString() + "]";
}

}
