package com.example.testdemo.bean;


public class Place {
private String city;
private String city_url;
private String sortLetter;
private String upperCase;
private String lowerCase;
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getCity_url() {
	return city_url;
}
public void setCity_url(String city_url) {
	this.city_url = city_url;
}
public String getSortLetter() {
	return sortLetter;
}
public void setSortLetter(String sortLetter) {
	this.sortLetter = sortLetter;
}
public String getUpperCase() {
	return upperCase;
}
public void setUpperCase(String upperCase) {
	this.upperCase = upperCase;
}
public String getLowerCase() {
	return lowerCase;
}
public void setLowerCase(String lowerCase) {
	this.lowerCase = lowerCase;
}
@Override
public String toString() {
	return "Place [city=" + city + ", city_url=" + city_url + ", sortLetter="
			+ sortLetter + ", upperCase=" + upperCase + ", lowerCase="
			+ lowerCase + "]";
}

}
