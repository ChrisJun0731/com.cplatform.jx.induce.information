package com.cplatform.jx.induce.information.traffic.info;

import java.util.List;

public class PathsInfo {

	  private String distance;
	  private String duration;
	  private String strategy;
	  private String tolls;
	  private String toll_distance;
	  private List<StepsInfo> steps;
	  private String restriction;
	  private String traffic_lights;
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	public String getTolls() {
		return tolls;
	}
	public void setTolls(String tolls) {
		this.tolls = tolls;
	}
	public String getToll_distance() {
		return toll_distance;
	}
	public void setToll_distance(String toll_distance) {
		this.toll_distance = toll_distance;
	}
	public List<StepsInfo> getSteps() {
		return steps;
	}
	public void setSteps(List<StepsInfo> steps) {
		this.steps = steps;
	}
	public String getRestriction() {
		return restriction;
	}
	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}
	public String getTraffic_lights() {
		return traffic_lights;
	}
	public void setTraffic_lights(String traffic_lights) {
		this.traffic_lights = traffic_lights;
	}
	  
	  
}
