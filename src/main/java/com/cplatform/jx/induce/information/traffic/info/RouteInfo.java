package com.cplatform.jx.induce.information.traffic.info;

import java.util.List;

public class RouteInfo {
	 private String origin;
	 private String destination;
	 private List<PathsInfo> paths;
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public List<PathsInfo> getPaths() {
		return paths;
	}
	public void setPaths(List<PathsInfo> paths) {
		this.paths = paths;
	}
	 
	 
}
