package com.cplatform.jx.induce.information.model.info;

public class ImgsInfo {

	private String imgName;
	private String imgPath;
	private int index;
	private String stayTime;
	private String type="img";

	public String getImgName() {
		return imgName;
	}
	public String getImgPath() {
		return imgPath;
	}
	public int getIndex() {
		return index;
	}
	public String getStayTime() {
		return stayTime;
	}
	public String getType() {
		return type;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public void setStayTime(String stayTime) {
		this.stayTime = stayTime;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
