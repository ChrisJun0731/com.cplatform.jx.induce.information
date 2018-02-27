package com.cplatform.jx.induce.information.traffic.info;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class AliWeatherDataInfo {

	private AliWeatherCityInfo city;
	private List<AliWeatherForecastInfo> forecast;
	public AliWeatherCityInfo getCity() {
		return city;
	}
	public List<AliWeatherForecastInfo> getForecast() {
		return forecast;
	}
	public void setCity(AliWeatherCityInfo city) {
		this.city = city;
	}
	public void setForecast(List<AliWeatherForecastInfo> forecast) {
		this.forecast = forecast;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
