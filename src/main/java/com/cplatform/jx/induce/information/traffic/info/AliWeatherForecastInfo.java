package com.cplatform.jx.induce.information.traffic.info;

import com.alibaba.fastjson.JSON;

public class AliWeatherForecastInfo {

	 private String conditionDay;// "多云",
	 private String conditionIdDay;// "1",
	 private String conditionIdNight;// "31",
	 private String conditionNight;// "多云",
	 private String predictDate;// "2016-09-01",
	 private String tempDay;// "27",
	 private String tempNight;// "18",
	 private String updatetime;// "2016-09-01 09:07:08",
	 private String windDirDay;// "西北风",
	 private String windDirNight;// "西北风",
	 private String windLevelDay;// "3",
	 private String windLevelNight;// "2"
	public String getConditionDay() {
		return conditionDay;
	}
	public String getConditionIdDay() {
		return conditionIdDay;
	}
	public String getConditionIdNight() {
		return conditionIdNight;
	}
	public String getConditionNight() {
		return conditionNight;
	}
	public String getPredictDate() {
		return predictDate;
	}
	public String getTempDay() {
		return tempDay;
	}
	public String getTempNight() {
		return tempNight;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public String getWindDirDay() {
		return windDirDay;
	}
	public String getWindDirNight() {
		return windDirNight;
	}
	public String getWindLevelDay() {
		return windLevelDay;
	}
	public String getWindLevelNight() {
		return windLevelNight;
	}
	public void setConditionDay(String conditionDay) {
		this.conditionDay = conditionDay;
	}
	public void setConditionIdDay(String conditionIdDay) {
		this.conditionIdDay = conditionIdDay;
	}
	public void setConditionIdNight(String conditionIdNight) {
		this.conditionIdNight = conditionIdNight;
	}
	public void setConditionNight(String conditionNight) {
		this.conditionNight = conditionNight;
	}
	public void setPredictDate(String predictDate) {
		this.predictDate = predictDate;
	}
	public void setTempDay(String tempDay) {
		this.tempDay = tempDay;
	}
	public void setTempNight(String tempNight) {
		this.tempNight = tempNight;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public void setWindDirDay(String windDirDay) {
		this.windDirDay = windDirDay;
	}
	public void setWindDirNight(String windDirNight) {
		this.windDirNight = windDirNight;
	}
	public void setWindLevelDay(String windLevelDay) {
		this.windLevelDay = windLevelDay;
	}
	public void setWindLevelNight(String windLevelNight) {
		this.windLevelNight = windLevelNight;
	}
	 
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
