package com.example.testdemo.bean;

import java.util.List;

public class DetailBean {
List<PlanBox> planBox;
List<DayInfo> dayInfo;
public List<PlanBox> getPlanBox() {
	return planBox;
}
public void setPlanBox(List<PlanBox> planBox) {
	this.planBox = planBox;
}
public List<DayInfo> getDayInfo() {
	return dayInfo;
}
public void setDayInfo(List<DayInfo> dayInfo) {
	this.dayInfo = dayInfo;
}
@Override
public String toString() {
	return "DetailBean [planBox=" + planBox + ", dayInfo=" + dayInfo + "]";
}

}
