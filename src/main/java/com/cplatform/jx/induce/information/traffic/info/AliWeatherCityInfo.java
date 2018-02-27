package com.cplatform.jx.induce.information.traffic.info;

import com.alibaba.fastjson.JSON;

public class AliWeatherCityInfo {

	 private int cityId;// 284609,
	 private String counname;// "中国",
	 private String name;// "东城区",
	 private String pname;// "北京市"
	public int getCityId() {
		return cityId;
	}
	public String getCounname() {
		return counname;
	}
	public String getName() {
		return name;
	}
	public String getPname() {
		return pname;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public void setCounname(String counname) {
		this.counname = counname;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	 
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
