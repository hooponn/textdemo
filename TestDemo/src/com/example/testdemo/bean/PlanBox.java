package com.example.testdemo.bean;

public class PlanBox {
String url;
int days;
String content;
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public int getDays() {
	return days;
}
public void setDays(int days) {
	this.days = days;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
@Override
public String toString() {
	return "PlanBox [url=" + url + ", days=" + days + ", content=" + content
			+ ", getUrl()=" + getUrl() + ", getDays()=" + getDays()
			+ ", getContent()=" + getContent() + ", getClass()=" + getClass()
			+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
			+ "]";
}

}
