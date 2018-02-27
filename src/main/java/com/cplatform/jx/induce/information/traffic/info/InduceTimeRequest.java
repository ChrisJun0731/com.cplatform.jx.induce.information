package com.cplatform.jx.induce.information.traffic.info;

import com.alibaba.fastjson.JSON;

public class InduceTimeRequest {

	//https://tp-restapi.amap.com/gate?sid=30011&reqData={"city":"城市代码","startpos":"起点","endpos":"终点","strategy":"0","waypoints":"途径点(可选)"}&serviceKey=密钥
	/**起始坐标*/
	private String startpos;
	/**终点坐标*/
    private String endpos;
    /**城市编码*/
	private String city;
	/**默认0*/
	private String strategy;
	/**途径点(可选)*/
	private String waypoints;
	public String getStartpos() {
		return startpos;
	}
	public void setStartpos(String startpos) {
		this.startpos = startpos;
	}
	public String getEndpos() {
		return endpos;
	}
	public void setEndpos(String endpos) {
		this.endpos = endpos;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	public String getWaypoints() {
		return waypoints;
	}
	public void setWaypoints(String waypoints) {
		this.waypoints = waypoints;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
	
}
