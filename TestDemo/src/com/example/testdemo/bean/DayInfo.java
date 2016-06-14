package com.example.testdemo.bean;

public class DayInfo {
String url;
String indexOfDay;
String date;
String titles;
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public String getIndexOfDay() {
	return indexOfDay;
}
public void setIndexOfDay(String indexOfDay) {
	this.indexOfDay = indexOfDay;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getTitles() {
	return titles;
}
public void setTitles(String titles) {
	this.titles = titles;
}
@Override
public String toString() {
	return "DayInfo [url=" + url + ", indexOfDay=" + indexOfDay + ", date="
			+ date + ", titles=" + titles + "]";
}

}
