package com.example.testdemo.bean;

public class Detail {
	public String day;
	public String date;
	public String about;
	public String content;
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Detail [day=" + day + ", date=" + date + ", about=" + about
				+ ", content=" + content + ", getDay()=" + getDay()
				+ ", getDate()=" + getDate() + ", getAbout()=" + getAbout()
				+ ", getContent()=" + getContent() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}
